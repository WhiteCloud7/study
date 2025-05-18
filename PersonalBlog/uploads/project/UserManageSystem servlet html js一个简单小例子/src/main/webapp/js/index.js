//加载表格数据
document.addEventListener("DOMContentLoaded",loadTableData);

async function loadTableData(){
    let userInfoDate = document.querySelector(".userInfo")
    let response = await fetch("userInfo")
    if(!response.ok){throw new Error(`HTTP 错误！状态码: ${response.status}`)}
    let json = await response.json();
    let tableData = json.map(userInfo =>`
    <tr>
        <td><input type="checkbox" class="userInfoCheckBox"></td>
        <td hidden class="userId">${userInfo.userId}</td>
        <td class="username">${userInfo.username}</td>
        <td class="sex">${userInfo.sex}</td>
        <td class="phone">${userInfo.phone}</td>
        <td class="email">${userInfo.email}</td>
    </tr>
`).join("");
    userInfoDate.innerHTML = tableData;
}

//获取操作类型值
function getSelected() {
    const operationType = document.getElementById("operationType");
    const selected = operationType.value;
    return selected;
}

//一个参数的查询
async function selectByOnePara(){
    let input = document.getElementById("searchContent");
    const para = input.value;
    const tableData = document.querySelector(".userInfo");
    const response = await fetch("selectOnePara",{
        method : "POST",
        headers: {"Content-Type": "text/plain"},
        body : para,
    });

    if(!response.ok)throw Error("请求失败！状态码："+response.status +"状态信息："+response.statusText);
    const json = await response.json();
    let selectResult = json.map( userInfo =>`
        <tr>
            <td><input type="checkbox" class="userInfoCheckBox"></td>
            <td hidden class="userId">${userInfo.userId}</td>
            <td>${userInfo.username}</td>
            <td>${userInfo.sex}</td>
            <td>${userInfo.phone}</td>
            <td>${userInfo.email}</td>
        </tr>
    `).join("");
    tableData.innerHTML = selectResult;
}

//多个参数的查询
async function selectByManyPara(){
    let input = document.getElementById("searchContent");
    const para = input.value;
    const tableData = document.querySelector(".userInfo");
    const response = await fetch("selectManyPara",{
        method : "POST",
        headers: {"Content-Type": "text/plain"},
        body : para,
    });
    if(!response.ok)throw Error("请求失败！状态码："+response.status +"状态信息："+response.statusText);

    const json = await response.json();
    let selectResult = json.map( userInfo =>`
        <tr>
            <td><input type="checkbox" class="userInfoCheckBox"></td>
            <td hidden class="userId">${userInfo.userId}</td>
            <td>${userInfo.username}</td>
            <td>${userInfo.sex}</td>
            <td>${userInfo.phone}</td>
            <td>${userInfo.email}</td>
        </tr>
    `).join("");
    tableData.innerHTML = selectResult;
}

//删除单个用户
async function deleteOneUser(){
    const checkBoxes = document.querySelectorAll(".userInfoCheckBox");
    let userId;
    for(let checkBox of checkBoxes){
        if(checkBox.checked){
            userId = checkBox.closest("tr").querySelector(".userId").textContent.trim();
        }
    }
    if(!userId) alert("选择一个用户！");
    else{
        const response = await fetch("deleteOneUser",{
            method : "POST",
            headers: {"Content-Type": "text/plain"},
            body : userId,
        });

        const result = await response.text();
        if(result === "删除成功！"){
            confirm("删除成功！");
            Back();
            loadTableData();
        }else if(result === "删除失败！"){
            alert("删除失败！");
        }else{
            alert("未知错误！");
        }
    }
}

//删除多个用户
async function deleteManyUser(){
    const checkBoxes = document.querySelectorAll(".userInfoCheckBox");
    let userId = [];
    for(let checkBox of checkBoxes){
        if(checkBox.checked){
            userId.push(checkBox.closest("tr").querySelector(".userId").textContent.trim());
        }
    }
    if(!userId) alert("选择一个用户！");
    else{
        const response = await fetch("deleteManyUser",{
            method : "POST",
            headers: {"Content-Type": "text/plain"},
            body : userId,
        });
        if (!response.ok) throw new Error("请求失败！状态码：" + response.status + " 状态信息：" + response.statusText);

        const result = await response.text();
        if(result === "删除成功！"){
            confirm("删除成功！");
            Back();
            loadTableData();
        }else if(result === "删除失败！"){
            alert("删除失败！");
        }else{
            alert("未知错误！");
        }
    }
}

//添加用户
document.getElementById('addUserForm').addEventListener('submit', async function (e) {
    e.preventDefault();
    const form = e.target;
    const formData = new FormData(form);
    try {
        const response = await fetch("addUser", {
            method: "POST",
            body: formData,
        });
        if (!response.ok) throw new Error("请求失败！状态码：" + response.status + " 状态信息：" + response.statusText);
        let addResult = await response.text();
        if (addResult === "添加成功！") {
            confirm("添加成功！");
            Back();
            loadTableData();
        } else if (addResult === "添加失败！") {
            alert("添加失败！");
        } else {
            alert("未知错误！");
        }
    } catch (error) {
        alert("发生错误：" + error.message);
    }
});

//更改用户
document.getElementById("alterUser").addEventListener('submit',async function updateUser(e){
    e.preventDefault();
    const form = e.target;
    const formDate = new FormData(form);
    const checkBoxes = document.querySelectorAll(".userInfoCheckBox");
    let userId;
    for(let checkBox of checkBoxes){
        if(checkBox.checked){
            userId = checkBox.closest("tr").querySelector(".userId").textContent.trim();
        }
    }
    formDate.append("alterUserId",userId);
    const response = await fetch("updateUser",{
        method : "POST",
        body : formDate,
        headers: {
            "Accept": "text/plain;charset=UTF-8"
        }
    });
    if (!response.ok) throw new Error("请求失败！状态码：" + response.status + " 状态信息：" + response.statusText);

    const result = await response.text();
    if(result==="更改成功！"){
        Back();
        loadTableData();
        confirm("更改成功！");
    }else if(result==="更改失败！"){
        alert("更改失败！");
    }else{
        alert("位置错误！");
    }
});

//检查是否有一个或多个空格，用于判断时使用单参数还是多参数查询
function SpaceCHeck(text){
    const hasSpace = /\s/.test(text);
    const hasManySpace = /\s{2,}/.test(text);

    if(hasSpace || hasManySpace)
        return true;
    else
        return false;
}

// 执行操作
function performOperation(){
    const selected = getSelected();
    const alterUserSection = document.getElementById("alterUserSection");
    const addUserSection = document.getElementById("addUserSection");
    const mask = document.getElementById("mask");
    const input = document.getElementById("searchContent");

    if(selected == "add") {
        addUserSection.hidden = false;
        mask.style.display = "block";
    }else if(selected == "alter") {
        const userInfoCheckBox = document.querySelectorAll(".userInfoCheckBox");
        let count = 0;
        for(let checked of userInfoCheckBox) {
            if(checked.checked) {
                count++;
            }
        }
        if(count < 1) {
            alert("请选择一个用户进行修改");
        }else if(count > 1){
            alert("选择的用户过多，请选择一个用户进行更改！")
        }else{
            defaultFrom();
            alterUserSection.hidden = false;
            mask.style.display = "block";
        }
    }else if(selected=="search"){
        if(SpaceCHeck(input.value))
            selectByManyPara();
        else
            selectByOnePara();
    }else if(selected=="delete"){
        const userInfoCheckBox = document.querySelectorAll(".userInfoCheckBox");
        let count = 0;
        for(let checked of userInfoCheckBox) {
            if(checked.checked) {
                count++;
            }
        }
        if(count == 1) {
            deleteOneUser();
        }else if(count > 1) {
            deleteManyUser();
        }else if(count < 1){
            alert("请选择一个用户！");
        }else{
            alert("未知错误！");
        }
    }
}

//添加和修改用户的返回键
function Back(){
    const selected = getSelected();
    const alterUserSection = document.getElementById("alterUserSection");
    const addUserSection = document.getElementById("addUserSection");
    const mask = document.getElementById("mask");
    if(selected == "add"){
        addUserSection.hidden = true;
        mask.style.display = "none";
    }else if(selected == "alter"){
        alterUserSection.hidden = true;
        mask.style.display = "none";
    }
}

//设置修改表单的默认值
function defaultFrom(){
    const checkBoxs = document.querySelectorAll(".userInfoCheckBox");

    let defaultUsername = document.getElementById("alterUsername");
    let defaultSex = document.getElementById("alterSex");
    let defaultPhone = document.getElementById("alterPhone");
    let defaultEmail = document.getElementById("alterEmail");

    for(let checkBox of checkBoxs){
        if(checkBox.checked){
            const currentUsername = checkBox.closest("tr").querySelector(".username").textContent.trim();
            const currentSex = checkBox.closest("tr").querySelector(".sex").textContent.trim();
            const currentPhone = checkBox.closest("tr").querySelector(".phone").textContent.trim();
            const currentEmail = checkBox.closest("tr").querySelector(".email").textContent.trim();

            defaultUsername.value = currentUsername;
            defaultSex.value = currentSex;
            defaultPhone.value = currentPhone;
            defaultEmail.value = currentEmail;
        }
    }
}