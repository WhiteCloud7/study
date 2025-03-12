function getSelected() {
    const operationType = document.getElementById("operationType");
    const selected = operationType.value;
    return selected;
}

async function selectByOnePara(){
    let input = document.getElementById("searchContent");
    const para = input.value;
    const tableData = document.querySelector(".userInfo");
    const response = await fetch("selectOnePara",{
        method : "POST",
        headers: {"Content-Type": "text/plain"},
        body : para,
    })

    if(!response.ok)throw Error("请求失败！状态码："+response.status +"状态信息："+response.statusText);
    const json = await response.json();
    let selectResult = json.map( userInfo =>`
        <tr>
            <td><input type="checkbox" class="userInfoCheckBox"></td>
            <td>${userInfo.username}</td>
            <td>${userInfo.sex}</td>
            <td>${userInfo.phone}</td>
            <td>${userInfo.email}</td>
        </tr>
    `).join("");
    tableData.innerHTML = selectResult;
}

async function selectByManyPara(){
    let input = document.getElementById("searchContent");
    const para = input.value;
    const tableData = document.querySelector(".userInfo");
    const response = await fetch("selectManyPara",{
        method : "POST",
        headers : {"Content-Type" : "text/plain"},
        body : para,
    });

    const json = await response.json();
    let selectResult = json.map( userInfo =>`
        <tr>
            <td><input type="checkbox" class="userInfoCheckBox"></td>
            <td>${userInfo.username}</td>
            <td>${userInfo.sex}</td>
            <td>${userInfo.phone}</td>
            <td>${userInfo.email}</td>
        </tr>
    `).join("");
    tableData.innerHTML = selectResult;
}

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
            alterUserSection.hidden = false;
            mask.style.display = "block";
        }
    }else if(selected=="search"){
        selectByOnePara();
    }
}

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