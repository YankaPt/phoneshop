function createForm(numberOfRows) {
    var myTable = "<div class=\"table-responsive\">\n" +
        "        <table id=\"table\" class=\"table table-striped table-bordered table-hover table-sm sortable\" cellspacing=\"0\"\n" +
        "               width=\"100%\">\n" +
        "            <thead>\n" +
        "            <tr>\n" +
        "                <th>Code</th>\n" +
        "                <th>Quantity</th>\n" +
        "            </tr>\n" +
        "            </thead>\n" +
        "            <tbody>";
    var i;
    for (i=0;i<numberOfRows;i++) {
        myTable += "<td>\n" +
            "                        <input type=\"text\" class=\"code-field\" id=code"+i+" name=\"quantity${phone.id}\"\n" +
            "                                value=\"${cartItems.get(status.index).quantity}\"/>\n" +
            "                        <br>\n" +
            "                        <c:if test=\"${errors.containsKey(phone.id)}\">\n" +
            "                            <label for=quantity${phone.id} class=\"error-message\" id=label${phone.id}>${errors.get(phone.id)}</label>\n" +
            "                        </c:if>\n" +
            "                    </td>" +
            "<td>\n" +
            "                        <input type=\"text\" class=\"quantity-field\" id=quantity${phone.id} name=\"quantity${phone.id}\"\n" +
            "                                value=\"${cartItems.get(status.index).quantity}\"/>\n" +
            "                        <br>\n" +
            "                        <c:if test=\"${errors.containsKey(phone.id)}\">\n" +
            "                            <label for=quantity${phone.id} class=\"error-message\" id=label${phone.id}>${errors.get(phone.id)}</label>\n" +
            "                        </c:if>\n" +
            "                    </td>"
    }
    myTable += "</tbody>\n" +
        "        </table>\n" +
        "        <input type=\"submit\" value=\"add to Cart\"/>\n" +
        "    </div>";
    document.write(myTable);
}