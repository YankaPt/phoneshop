function addToCart(phoneId, quantity, locale) {
    var data = {phoneId: phoneId, quantity: quantity};
    $.ajax({
        headers: {
            'Accept': 'application/json',
            'Content-Type': 'application/json',
            'Locale': locale
        },
        url: "ajaxCart",
        method: "POST",
        dataType: 'json',
        data: JSON.stringify(data)
    })
        .done(function (data, textStatus, jqXHR) {
            setLabel(phoneId, quantity, data.message);
            if (data.hasOwnProperty('cartItemsAmount')) {
                var cartInfo = "My cart: " + data.cartItemsAmount + " items $" + data.cartItemsPrice;
                $('#cart').text(cartInfo);
            }
        })
        .fail(function (jqXHR, textStatus, errorThrown) {
            $('#result').text("Something is wrong: " + textStatus + " " + errorThrown);
        });
}

function setLabel(phoneId, quantity, message) {
    if (message === 'success') {
        document.getElementById("label" + phoneId).innerHTML = '<p style="color: green">Successfully adding ' + quantity + ' products to cart</p>';
    } else {
        document.getElementById("label" + phoneId).innerHTML = '<p style="color: red">' + message + '</p>';
    }
}

function doSearch(searchText) {
    location.href='?search='+searchText
}

function doSort(pageNumber, orderBy, isAscend, reverseAscend) {
    if (reverseAscend) {
        isAscend = !isAscend;
    }
    location.href = "?pageNumber="+pageNumber+"&orderBy="+orderBy+"&isAscend="+isAscend;
}