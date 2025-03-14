function setCookie(name,value,day){
    let deadline;
    if(day){
        const date = new Date();
        date.setTime(date.getTime() + day*24*60*60*1000);
        deadline = ";expires=" + date.toUTCString();
        document.cookie = `${name}=${value}${deadline};path=/`;

    }else
        console.log("请设置过期时间！");
}

function getCookieValue(name){
    name = name + "=";
    let cookie = document.cookie.split(";");
    for(let i=0;i<cookie.length;i++){
        let cookieDate = cookie[i].trim();
        if(cookieDate.includes(name))
            return cookieDate.substring(name.length , cookieDate.length);
    }
    return "";
}

function setObjectCookie(object,day){
    let deadline;
    for(const [key,value] of Object.entries(object)){
        if(day){
            let date = new Date();
            date.setTime(date.getTime() + day*1000*60*60*24)
            deadline = ";expires= "+ date.toUTCString() + ";";
            document.cookie = `${key}=${value}${deadline};path=/`;
        }else
            console.log("请设置过期时间！");
    }
}

function getManyCookie(keySet){
    let cookie = document.cookie.split(";");
    for(let key of keySet){
        key = key + "=";
        for(let i=0;i<cookie.length;i++){
            let cookieDate = key[i].trim();
            if(cookieDate.includes(name))
                return cookieDate.substring(name.length , cookieDate.length);
            else
                return "";
        }
    }
}

function getKeySetCookie(){
    let cookie = document.cookie.split(";");
    let KeySet = [];
    for(let i=0;i<cookie.length;i++){
        let cookieDate = cookie[i].trim();
        if(cookieDate && cookieDate.includes("="))
            KeySet.push(cookieDate.split("=")[0]);
    }
    return KeySet;
}