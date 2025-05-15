import java.io.File;
import java.util.LinkedList;
import java.util.Queue;

private static void deleteIteratively(File dir) {
    System.out.println("[DEBUG] 开始删除目录: " + dir.getPath());
    if (dir == null || !dir.exists()) {
        System.out.println("[INFO] 目录不存在或为空: " + (dir != null ? dir.getPath() : "null"));
        return;
    }

    Queue<File> queue = new LinkedList<>();
    queue.add(dir);
    int totalFiles = 0;
    int totalDirs = 0;
    long startTime = System.currentTimeMillis();

    System.out.println("[INFO] 开始删除目录: " + dir.getPath());

    while (!queue.isEmpty()) {
        File current = queue.poll();
        System.out.println("[DEBUG] 处理目录: " + current.getPath());

        File[] files = current.listFiles();
        if (files == null) {
            System.out.println("[WARN] 无法列出目录内容: " + current.getPath() + " (可能是权限问题)");
            continue;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("[DEBUG] 发现子目录: " + file.getPath());
                queue.add(file);
                totalDirs++;
            } else {
                System.out.print("[INFO] 删除文件: " + file.getPath());
                if (file.delete()) {
                    System.out.println(" -> 成功");
                    totalFiles++;
                } else {
                    System.out.println(" -> 失败 (可能被占用或权限不足)");
                }
            }
        }

        // 删除空目录
        System.out.print("[INFO] 删除空目录: " + current.getPath());
        if (current.delete()) {
            System.out.println(" -> 成功");
        } else {
            System.out.println(" -> 失败 (可能非空或权限不足)");
        }
    }

    long endTime = System.currentTimeMillis();
    System.out.println("[INFO] 删除完成");
    System.out.println("[INFO] 总耗时: " + (endTime - startTime) + "ms");
    System.out.println("[INFO] 共删除: " + totalFiles + " 个文件, " + totalDirs + " 个目录");
}

public static void main(String[] args) {
    String rootPath = "D:\\GitHub\\study\\tmp\\uploads\\project\\一级目录"; // 替换为实际路径
    try {
        deleteDirectory(rootPath);
        System.out.println("目录删除成功");
    } catch (IOException e) {
        System.err.println("删除失败：" + e.getMessage());
    }
}