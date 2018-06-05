var getDateAnchor =  function getDateAnchor (date){
    var year = date.getFullYear().toString();
    var month = (date.getMonth() + 1).toString();
    var day = date.getDate().toString();
    return "#" + year + ((month.length>1) ? month : ("0" + month)) + ((day.length>1) ? day : ("0" + day));
}

var loadContent = function loadContent (filename){
    $("main").load(filename + " table", function() {
        var offset = $("nav").outerHeight();
        var anchor = getDateAnchor(new Date());
        var el = $(anchor);
        if (el.length > 0){
            el.removeClass("table-active");
            el.addClass("table-success");
            $('html, body').animate({
                scrollTop: (el.offset().top - offset)
            }, 1000);
        }
      });
}

var init = function init(){
    var hash = window.location.hash;
    var navEl = $("li.nav-item").removeClass("active");

    if (hash === "#martin"){
        loadContent("3.html");
        navEl.eq(0).addClass("active");
    }
    else if (hash === "#vladka"){
        loadContent("17.html");
        navEl.eq(1).addClass("active");
    }
    else if (hash === "#misa"){
        loadContent("21.html");
        navEl.eq(2).addClass("active");
    }
    else{
        window.location.hash = "#martin"
        loadContent("3.html");
        navEl.eq(0).addClass("active");
    }
}

$(document).ready(init);
$(window).on('hashchange',init);
