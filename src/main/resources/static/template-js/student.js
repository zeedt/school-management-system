function previousPage() {


    var pageData = $("#currentPage").html();
    // var filter = $("#transfer-filter").val();
    var pageNo = parseInt(pageData);
    if (pageNo < 1) {
        return;
    }

    loadStudentPage(parseInt(pageNo-1));

}

function nextPage() {
    var pageData = $("#currentPage").html();
    // var filter = $("#transfer-filter").val();
    var pageNo = parseInt(pageData);
    var totalNoOfPagesData = $("#totalPageNo").html();
    var totalNoOfPages = parseInt(totalNoOfPagesData);
    if ((pageNo+1) >= totalNoOfPages) {
        return;
    }

    loadStudentPage(parseInt(pageNo+1));
}

function loadStudentPage (pageNo, filter) {
    window.location.href ="/studentlist?pageNo="+pageNo;
}

