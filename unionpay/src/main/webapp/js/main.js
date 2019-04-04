/**
 * Created by fengsujing on 17/2/23.
 */
$(document).ready(function(){
    $(".see-or").click(function(){
        $(this).attr('src',$(this).attr('src')=='images/ico/see.png'?'images/ico/nosee.png':'images/ico/see.png');
    })

    $(".palul>li").click(function(){
        $(this).addClass("cur").siblings().removeClass("cur");
        var nIndex=$(this).index();
        $(this).parent().siblings().removeClass("palcont-cur").eq(nIndex).addClass("palcont-cur");
    });
    $(".show").click(function(){
        $(this).parent("div.prod-spec").hide();
        return false;
    })
})

function alertMsg(msg) {
	$("#checkModel").reveal({
		animation:"fade"
	});
	$("#checkMsg").html(msg);
}