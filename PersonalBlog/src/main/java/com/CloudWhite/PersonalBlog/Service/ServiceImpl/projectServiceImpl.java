package com.CloudWhite.PersonalBlog.Service.ServiceImpl;

import com.CloudWhite.PersonalBlog.Dao.mybatisDao;
import com.CloudWhite.PersonalBlog.Dao.projectDao;
import com.CloudWhite.PersonalBlog.Entity.project;
import com.CloudWhite.PersonalBlog.Model.UserContext;
import com.CloudWhite.PersonalBlog.Service.projectService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
@Transactional
public class projectServiceImpl implements projectService {
    private String baseDir = System.getProperty("user.dir") + "/uploads/project";
    private projectDao projectDao;
    @PersistenceContext
    private EntityManager entityManager;
    private mybatisDao mybatisDao;

    @Autowired
    public projectServiceImpl(projectDao projectDao,mybatisDao mybatisDao) {
        this.projectDao = projectDao;
        this.mybatisDao = mybatisDao;
    }

    private String getAbsolutePath(String filePath, String fileName) {
        if (filePath == null || filePath.isEmpty()) {
            return baseDir + "/" + fileName;
        }
        return baseDir + "/" + filePath.replace(",", "/") + "/" + fileName;
    }

    public List<project> getTheFirstDirectory() {
        return projectDao.getTheFirstDirectory();
    }

    public List<project> findAllByCurrentDirId(int currentDirId) {
        return projectDao.findAllByParentDirId(currentDirId);
    }

    public int getCurrentFileId(String[] filePathArray, int loopTimes) {
        int currentFileId = 0;
        if (loopTimes > 0) {
            currentFileId = projectDao.getFileIdByFileName(filePathArray[0]);
        }
        for (int i = 1; i < loopTimes; i++) {
            currentFileId = projectDao.getNextFileIdByParentIdAndFileName(currentFileId, filePathArray[i]);
        }
        return currentFileId;
    }

    public List<project> getFilesByRouter(String filePath) {
        if (filePath == null || filePath.isEmpty()) {
            return null;
        }
        String[] filePathArray = filePath.split("/");
        int currentFileId = getCurrentFileId(filePathArray, filePathArray.length);
        return findAllByCurrentDirId(currentFileId);
    }

    public project addFile(String fileName, String modifyTime, String type, String filePath) throws IOException {
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        String[] filePathArray = filePath.split(",");
        byte dirLevel = filePathArray[0].isEmpty() ? 1 : (byte) (filePathArray.length + 1);

        project project = new project();
        project.setFileName(fileName);
        project.setModifyTime(modifyTime);
        project.setType(type);
        project.setDirLevel(dirLevel);
        int parentDirId = filePathArray[0].isEmpty() ? 0 :
                getCurrentFileId(filePathArray, filePathArray.length);
        project.setParentDirId(parentDirId);
        projectDao.save(project);

        File file = new File(getAbsolutePath(filePath,fileName));
        if(!file.exists()&&!fileName.contains("."))
            file.mkdirs();
        else if(!file.exists()&&fileName.contains("."))
            file.createNewFile();
        project newProject = projectDao.getNextDirsByParentIdAndFileName(parentDirId, fileName);
        return newProject;
    }

    public void rename(String newFileName, int fileId, String fPath) {
        project proj = projectDao.findByFileId(fileId);
        String oldName = proj.getFileName();

        File oldFile = new File(getAbsolutePath(fPath,oldName));
        File newFile = new File(getAbsolutePath(fPath,newFileName));
        oldFile.renameTo(newFile);

        proj.setFileName(newFileName);
        projectDao.save(proj);
    }


    public void deleteFile(int[] deleteFiles,String filePath) {
        List<Integer> fileIdList = Arrays.stream(deleteFiles).boxed().toList();
        for (int fileId : deleteFiles) {
            File file;
            if(filePath.equals(""))
                file = new File(getAbsolutePath(filePath,projectDao.findByFileId(fileId).getFileName()));
            else
                file = new File(getAbsolutePath(filePath,projectDao.findByFileId(fileId).getFileName()));
            if (file.exists()) {
                if (file.isDirectory()) deleteDirectoryRecursively(file);
                else file.delete();
            }
            deleteChildrenFile(fileId);
        }
        projectDao.deleteAllById(fileIdList);
    }

    private void deleteDirectoryRecursively(File dir) {
        for (File f : Objects.requireNonNull(dir.listFiles())) {
            if (f.isDirectory()) deleteDirectoryRecursively(f);
            else f.delete();
        }
        dir.delete();
    }

    public void deleteChildrenFile(int deleteFile) {
        List<project> children = projectDao.findAllByParentDirId(deleteFile);
        if (children != null) {
            for (project child : children) {
                projectDao.deleteById(child.getFileId());
                deleteChildrenFile(child.getFileId());
            }
        }
    }

    public project saveNewProject(int fileId, byte dirLevel, int parentDirId) {
        project project = projectDao.findByFileId(fileId);
        project newProject = new project();
        newProject.setDirLevel(dirLevel);
        newProject.setFileName(project.getFileName());
        newProject.setType(project.getType());
        LocalDateTime dateTime = LocalDateTime.now();
        newProject.setModifyTime(dateTime.toString());
        newProject.setParentDirId(parentDirId);
        entityManager.clear();
        projectDao.save(newProject);
        return projectDao.getNextDirsByParentIdAndFileName(parentDirId, project.getFileName());
    }

    public List<project> copyPaste(int[] fileIds, String filePath, String shearPath) throws IOException {
        List<project> result = new ArrayList<>();
        filePath = URLDecoder.decode(filePath, StandardCharsets.UTF_8);
        String[] filePathArray = filePath.split(",");
        byte dirLevel = filePathArray[0].equals("") ? 1 : (byte) (filePathArray.length + 1);
        int parentDirId = filePathArray[0].equals("") ? 0 : getCurrentFileId(filePathArray, filePathArray.length);

        for (int fileId : fileIds) {
            project origin = projectDao.findByFileId(fileId);
            String sourcePath = getAbsolutePath(shearPath, origin.getFileName());
            String targetPath = getAbsolutePath(filePath, origin.getFileName());
            if (targetPath.startsWith(sourcePath)) {
                throw new IOException("目标路径不能是源路径的子目录，避免无限嵌套！");
            }
            project newProject = saveNewProject(fileId, dirLevel, parentDirId);
            result.add(newProject);
            // 本地文件复制
            copyFileOrDirectory(sourcePath, targetPath);
            // 复制子目录
            result.addAll(copyChildrenDir(fileId, (byte) (dirLevel + 1), newProject.getFileId(),
                    filePath + "/" + newProject.getFileName(),
                    shearPath + "/" + origin.getFileName()));
        }
        return result;
    }

    public List<project> copyChildrenDir(int fileId, byte dirLevel, int parentId,
                                         String targetPath, String sourcePath) throws IOException {
        List<project> result = new ArrayList<>();
        List<project> children = projectDao.findAllByParentDirId(fileId);
        if (children != null) {
            for (project child : children) {
                project newProject = saveNewProject(child.getFileId(), dirLevel, parentId);
                result.add(newProject);
                String sourceChildPath = getAbsolutePath(sourcePath, child.getFileName());
                String targetChildPath = getAbsolutePath(targetPath, newProject.getFileName());
                copyFileOrDirectory(sourceChildPath, targetChildPath);
                // 递归处理
                result.addAll(copyChildrenDir(child.getFileId(), (byte) (dirLevel + 1),
                        newProject.getFileId(),
                        targetPath + "/" + newProject.getFileName(),
                        sourcePath + "/" + child.getFileName()));
            }
        }
        return result;
    }

    public void copyFileOrDirectory(String sourcePath, String targetPath) throws IOException {
        File source = new File(sourcePath);
        File target = new File(targetPath);
        if (source.isDirectory()) {
            // 递归复制目录
            Files.walk(source.toPath())
                    .forEach(src -> {
                        Path dest = target.toPath().resolve(source.toPath().relativize(src));
                        try {
                            if (Files.isDirectory(src)) {
                                Files.createDirectories(dest);
                            } else {
                                Files.copy(src, dest, StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (IOException e) {
                            e.printStackTrace(); // 可做更优日志处理
                        }
                    });
        } else {// 复制单个文件
            Files.createDirectories(target.getParentFile().toPath()); // 确保目标目录存在
            Files.copy(source.toPath(), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public List<project> cutPaste(int[] fileIds, String filePath,String shearPath) throws IOException {
        List<project> projectList = copyPaste(fileIds, filePath,shearPath);
        deleteFile(fileIds,shearPath);
        return projectList;
    }

    public String uploadFile(MultipartFile multipartFile,String fPath) {
        fPath = URLDecoder.decode(fPath, StandardCharsets.UTF_8);
        String[] filePathArray = fPath.split(",");
        byte dirLevel = filePathArray[0].equals("") ? 1 : (byte) (filePathArray.length + 1);
        int parentDirId = filePathArray[0].equals("") ? 0 :
                getCurrentFileId(filePathArray, filePathArray.length);

        if (multipartFile.isEmpty())
            return "上传文件为空";

        String originalFilename = multipartFile.getOriginalFilename();
        String uploadDir = baseDir;
        Path path = Paths.get(uploadDir);

        try {
            if (!Files.exists(path))
                Files.createDirectories(path);

            Path filePath = path.resolve(originalFilename);
            // 判断是否为 zip 文件
            String type = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);
            if (type.equalsIgnoreCase("zip")) {
                multipartFile.transferTo(filePath.toFile());
                handleZipAndSaveToDatabase(filePath, uploadDir, dirLevel, parentDirId,fPath);
            } else {
                if(!projectDao.existsByParentDirIdAndFileName((byte) parentDirId,originalFilename)){
                    LocalDateTime modifyTime = LocalDateTime.now();
                    project project = new project(originalFilename,modifyTime.toString(),type,dirLevel,parentDirId);
                    projectDao.save(project);
                }
                fPath = fPath.replaceAll(",","/");
                String lastUpdir = baseDir+fPath+originalFilename;
                multipartFile.transferTo(Paths.get(lastUpdir).toFile());
            }
            return "上传成功";
        } catch (IOException e) {
            e.printStackTrace();
            return "上传失败";
        }
    }

    private void handleZipAndSaveToDatabase(Path zipFilePath, String uploadDir, byte dirLevel, int parentDirId,String fPath) throws IOException {
        // 1. 获取 zip 文件名作为顶级目录名
        String zipBaseName = zipFilePath.getFileName().toString();
        String topLevelDirName = zipBaseName.contains(".") ? zipBaseName.substring(0, zipBaseName.lastIndexOf('.')) : zipBaseName;
        // 2. 创建顶级目录文件夹
        File topLevelDir = new File((uploadDir+"/"+fPath.replace(",","/")).replace("\\","/"), topLevelDirName);
        if (!topLevelDir.exists()) {
            topLevelDir.mkdirs();
        }
        // 3. 插入数据库：顶级目录记录
        LocalDateTime modifyTime = LocalDateTime.now();
        project topLevelProject = new project(topLevelDirName, modifyTime.toString(), "文件夹", (dirLevel), parentDirId);
        mybatisDao.saveProject(topLevelProject);
        int topLevelId = projectDao.getNextFileIdByParentIdAndFileName(parentDirId,topLevelDirName);

        // 4. 解压并构建路径映射
        Map<String, Integer> pathToIdMap = new HashMap<>();
        Set<String> createdDirs = new HashSet<>();

        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath.toFile()))) {
            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String entryName = zipEntry.getName();
                if (entryName.endsWith("/") || entryName.endsWith("\\")) {
                    entryName = entryName.substring(0, entryName.length() - 1);
                }

                // 解压到顶级目录下
                File outFile = new File(topLevelDir, entryName);
                if (zipEntry.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    File parent = outFile.getParentFile();
                    if (parent != null && !parent.exists()) {
                        parent.mkdirs();
                    }
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipInputStream.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }

                // 构建数据库结构
                String[] pathParts = entryName.split("/|\\\\");
                StringBuilder currentPath = new StringBuilder();

                int currentParentId = topLevelId;
                byte baseLevel = (byte)(dirLevel + 1);

                for (int i = 0; i < pathParts.length; i++) {
                    String name = pathParts[i];
                    currentPath.append("/").append(name);
                    String fullPath = currentPath.toString();
                    boolean isLast = (i == pathParts.length - 1);
                    boolean isDirectory = !isLast || zipEntry.isDirectory();

                    if (isDirectory && createdDirs.contains(fullPath)) {
                        currentParentId = pathToIdMap.get(fullPath);
                        continue;
                    }
                    if (!isDirectory && pathToIdMap.containsKey(fullPath)) {
                        continue;
                    }
                    String type = isDirectory ? "文件夹" :
                            (name.contains(".") ? name.substring(name.lastIndexOf('.') + 1) : "未知");
                    project proj = new project(name, modifyTime.toString(), type, (byte)(baseLevel + i), currentParentId);
                    mybatisDao.saveProject(proj);
                    currentParentId = projectDao.getNextFileIdByParentIdAndFileName(currentParentId,name);

                    pathToIdMap.put(fullPath, currentParentId);
                    if (isDirectory) {
                        createdDirs.add(fullPath);
                    }
                }
                zipInputStream.closeEntry();
            }
        } finally {
            Files.deleteIfExists(zipFilePath);
        }
    }
}
