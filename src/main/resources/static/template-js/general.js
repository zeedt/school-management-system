function loadUrl(url) {
    window.location.href = url + "?access_token="+localStorage.getItem("access_token");
}