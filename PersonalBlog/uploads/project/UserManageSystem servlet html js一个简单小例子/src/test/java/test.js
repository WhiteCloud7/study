class  person{
    name = "你爹";
    age = 99;
    l = 180 ;
    constructor(name, age){
        this.name = name;
        this.age = age; 
    }
    
    getName(){
        return this.age;
    }
}
person.prototype.toString = function(){
    return this.name + " " + this.age;
}

class student extends person{
    constructor(name, age, score){
        super(name, age);
        this.score = score;
    }

    getName(){
        return this.l;
    }
}

const p = new person("张三", 18);
const s = new student("李四", 19, 100);
console.log(p.getName());
console.log(s.getName());