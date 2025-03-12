document.addEventListener("DOMContentLoaded", loadTableData);

async function loadTableData(){
    let userInfoDate = document.querySelector(".userInfo")
    let response = await fetch("userInfo")
    if(!response.ok){throw new Error(`HTTP 错误！状态码: ${response.status}`)}
    let json = await response.json();
    let tableData = json.map(userInfo =>`
    <tr>
        <td><input type="checkbox" class="userInfoCheckBox"></td>
        <td>${userInfo.username}</td>
        <td>${userInfo.sex}</td>
        <td>${userInfo.phone}</td>
        <td>${userInfo.email}</td>
    </tr>
`).join("");
    userInfoDate.innerHTML = tableData;
}