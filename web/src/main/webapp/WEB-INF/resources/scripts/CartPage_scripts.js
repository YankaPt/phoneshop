function deleteItem(phoneId) {
    document.getElementById('delete'+phoneId).removeAttribute("disabled")
    document.getElementById('delete'+phoneId).value = phoneId;
}
function update() {
    [].forEach.call(document.getElementsByClassName("quantity-field"), function (element) {
        var clonedElement = element.cloneNode();
        clonedElement.setAttribute("name", element.getAttribute("name").substring(8));
        document.getElementById("updateSubmit").appendChild(clonedElement);
    });
}