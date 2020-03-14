function previousPage() {


    var pageData = $("#currentPage").html();
    // var filter = $("#transfer-filter").val();
    var pageNo = parseInt(pageData);
    if (pageNo < 1) {
        return;
    }

    loadRegistrationPage(parseInt(pageNo-1));

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

    loadRegistrationPage(parseInt(pageNo+1));
}

function loadRegistrationPage (pageNo, filter) {
    window.location.href ="/registrationlist?pageNo="+pageNo;
}

