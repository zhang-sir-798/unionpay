/**
 * Created by fengsujing on 17/2/23.
 */
$(document).ready(function(){
    $.ajax({
        type:'get',
        url:'http://api.map.baidu.com/telematics/v3/weather?output=json&ak=0A5bc3c4fb543c8f9bc54b77bc155724&location=%E4%B8%8A%E6%B5%B7',
        dataType:'jsonp',
        success:function(data){
            console.log(data.results[0].weather_data[0]);
            /*模版渲染*/
            var html = data.results[0].weather_data[0].date;
            var day = data.results[0].weather_data[0].dayPictureUrl;
            var night = data.results[0].weather_data[0].nightPictureUrl;
            $('.temp').html(html);
            $('.day').attr('src',day);
            $('.night').attr('src',night);

        }
    });

});
window.itcast = {};
//解决移动端click事件延迟问题
itcast.tap = function(dom,callback){
    if(!dom || typeof dom != 'object' ) return false;
    /*基本的判断*/
    var startTime = 0;
    var isMove = false;
    dom.addEventListener('touchstart',function(e){
        startTime = Date.now();
    });
    dom.addEventListener('touchmove',function(e){
        isMove = true;
    });
    dom.addEventListener('touchend',function(e){
        if((Date.now()-startTime)<300 && !isMove){
            callback && callback(e);
        }
        /*重置参数*/
        startTime = 0;
        isMove = false;
    });
};

var list = document.getElementById("list");
//大显示屏
var content = document.getElementById("content");
//小显示屏
var sum = document.getElementById("sum");
//获取数字部分
var nums = document.getElementsByClassName("num");
//获取符号部分
var signs = document.getElementsByClassName("sign");
var lis = list.children;
var calc = document.getElementById("calc");
var save = calc.nextElementSibling;
var title = save.querySelector("h3");
var hisList = save.querySelector(".hisList");
//公式
var formula = "";
var str = "";
//记录上一次触发的事件是否是等号运算
var equal = false;
//数字部分显示
for(var i=0;i<nums.length;i++){
    var num = nums[i];
    itcast.tap(num, function (e) {
        //如果上一次是等号运算，清空formula
        if(equal){
            formula = "";
        }
        //解决eval函数0开头时的bug
        if(e.target.innerHTML==="0"){
            var c = formula.charAt(formula.length-1);
            if(c==="+"||c==="-"||c==="*"||c==="/"||c==="%"){
                return false;
            }else if(str===""){
                return false;
            }else{
                str += e.target.innerHTML;
                formula += e.target.innerHTML;
                content.setAttribute("value",str);
                sum.setAttribute("value",formula);
            }
        }else if(e.target.innerHTML==="."){
            if(str===""){
                str = "0" + e.target.innerHTML;
                formula += str;
            }

            if(formula.indexOf(".")== -1){
                str += e.target.innerHTML;
                formula += e.target.innerHTML;
            }
            content.setAttribute("value",str);
            sum.setAttribute("value",formula);

        }else{
            str += e.target.innerHTML;
            formula += e.target.innerHTML;
            content.setAttribute("value",str);
            sum.setAttribute("value",formula);
        }
        equal = false;

    });
}
//运算符部分
for(var i=0;i<signs.length;i++){
    var sign = signs[i];
    itcast.tap(sign, function (e) {
        //直接输入运算符时的bug
        if(formula===""){
            formula = "0"+e.target.innerHTML;
        }
        var c = formula.charAt(formula.length-1);
        //判断最后一位是不是运算符，是的话就替换
        if(c==="+"||c==="-"||c==="*"||c==="/"||c==="%"){
            formula = formula.substring(0,formula.length-1);
            formula += e.target.innerHTML;
            sum.setAttribute("value",formula);
        }else if(c==="."){
            return false;
        }else{
            //计算出结果
            content.setAttribute("value",eval(formula));
//               formula = String(eval(formula));
            formula += e.target.innerHTML;
            sum.setAttribute("value",formula);
            str = "";
        }
        equal = false;
    });
}

//AC清除还原功能
lis[3].onclick = function () {
    str = "";
    formula = "";
    content.setAttribute("value","0");
    sum.setAttribute("value","0");
    equal = false;
}


//公式存储功能
var saveHeight = save.offsetHeight;
var isMove = false;
var his = document.querySelector(".history");
his.onclick = function () {
    if(!isMove){
        addTransition();
        setTranslateY(-saveHeight);
        if(datas.length==0){
            hisList.innerHTML = "尚无历史记录";
        }else{
            hisList.innerHTML = "";
            //刷新列表
            createLi(hisList);
            var btn = document.createElement("button");
            btn.className = "clear";
            hisList.appendChild(btn);
            btn.innerText = "清空";
            btn.onclick = function () {
                datas = [];
                hisList.innerHTML = "尚无历史记录";
            }
        }
        isMove = true;
    }else{
        addTransition();
        setTranslateY(0);
        isMove = false;
    }
}
//滑动手势移除save
var startY = 0;
var moveY = 0;
var distanceY = 0;
//公用方法
//过渡
var addTransition = function () {
    save.style.transition = "all 0.5s";
    save.style.webkitTransition = "all 0.5s";
}
//移除过渡
var removeTransition = function () {
    save.style.transition = "none";
    save.style.webkitTransition = "none";
}
//移动
var setTranslateY = function (translateY) {
    save.style.transform = "translate3d(0,"+translateY+"px,0)";
    save.style.webkitTransform = "translate3d(0,"+translateY+"px,0)";
}

title.addEventListener('touchstart', function (e) {
    startY = e.touches[0].clientY;
});
title.addEventListener('touchmove', function (e) {
    moveY = e.touches[0].clientY;
    distanceY = moveY-startY;
    removeTransition();
    if(distanceY<=0){
        return false;
    }else{
        setTranslateY(-saveHeight+distanceY);
    }
});
title.addEventListener('touchend', function (e) {
    if(distanceY<(saveHeight/3)){
        addTransition();
        setTranslateY(-saveHeight);
    }else{
        addTransition();
        setTranslateY(0);
        isMove = false;
    }
    startY = 0;
    moveY = 0;
    distanceY = 0;
});

//等号运算
var datas = [];
itcast.tap(list.lastElementChild, function () {
    //直接输入运算符时的bug
    if(formula===""){
        return;
    }
    var c = formula.charAt(formula.length-1);
    if(c==="+"||c==="-"||c==="*"||c==="/"||c==="%"){
        return;
    }
    //历史记录
    datas[datas.length] = formula;

    content.setAttribute("value",eval(formula));
    sum.setAttribute("value","");
    formula = String(eval(formula));
    str = "";
    equal = true;
});

//        动态创建历史记录
function createLi(Dom){
    //动态创建元素-ul    仅仅是在内存中创建了一个ul的对象
    var ul = document.createElement("ul");
    //把ul对象添加到页面上显示
    Dom.appendChild(ul);


    for(var i = datas.length-1; i >= 0; i--) {
        var data = datas[i];
        //创建li
        var li = document.createElement("li");
        //把li添加到ul中
        ul.appendChild(li);
        li.innerText = data;
//        setInnerText(li, data);

        li.onclick = function (e) {
            formula = this.innerHTML;
            sum.setAttribute("value",formula);
            e.stopPropagation();
        }
    }
}