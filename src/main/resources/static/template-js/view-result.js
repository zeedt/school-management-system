function previousPage() {


    var pageData = $("#currentPage").html();
    // var filter = $("#transfer-filter").val();
    var pageNo = parseInt(pageData);
    if (pageNo < 1) {
        return;
    }

    searchForResultWithPage(parseInt(pageNo-1));

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

    searchForResultWithPage(parseInt(pageNo+1));
}

function loadRegistrationPage (pageNo, filter) {
    window.location.href ="/registrationlist?pageNo="+pageNo;
}

function searchForResult(pageNo, filter) {

    var subjectCode = $("#subjectCode").val();
    var sClass = $("#sClass").val();

    window.location.href ="/view-results?subjectCode="+subjectCode+"&sClass="+sClass;

}
function searchForResultWithPage(pageNo, filter) {

    var subjectCode = $("#subjectCode").val();
    var sClass = $("#sClass").val();

    pageNo = (pageNo == undefined || pageNo == null || pageNo == '') ? 0 : pageNo;

    window.location.href ="/view-results?subjectCode="+subjectCode+"&sClass="+sClass+"&pageNo="+pageNo;

}

