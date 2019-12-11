ENTITIES['order'] = {
    name: 'Заказы',
    url: 'orders',
    // будет определена в createEntityContainers
    reloadFunction: {},
    // будет заполнено при загрузке данных в таблицы
    rows: [],
    actions:[
        {
            name:"Добавить",
            fields: [
                {
                    name: "Продукт",
                    generator: function(order) {
                        var select = $('<select/>').attr('id', 'prodSelect'+order.id)
                        $.each(ENTITIES['product'].rows, function(i, product){
                            select.append(
                                $('<option/>').attr('value', product.id).text(product.name)
                            );
                        });
                        return select;
                    }
                },
                {
                    name: "Количество",
                    generator: function(order) {
                        var select = $('<select/>').attr('id', 'quant'+order.id);
                        for(i=1;i<=10;i++) {
                            select.append(
                                $('<option/>').attr('value', i).text(i)
                            );
                        }
                        return select;
                    }
                }
            ],
            isVisible:function(order) {return order.status == "ACTUAL" || order.status == "NEW"},
            onclick:function(event) {
                var order = event.data.obj;
                var productId = $('#prodSelect'+order.id).val();
                var quant = $('#quant'+order.id).val();
                addProductsToOrder(order, productId, quant);
            }
        },
        {
            name:'Купить сейчас',
            isVisible:function(order) {return order.status == "ACTUAL"},
            onclick:function(event) { finishOrder(event.data.obj); }
        },
        {
            name:'Купить через 10 секунд',
            isVisible:function(order) {return order.status == "ACTUAL"},
            onclick:function(event) {scheduleOnce(event.data.obj, 10); }
        },
        {
            name:'Покупать каждые 20 секунд',
            isVisible:function(order) {return order.status == "ACTUAL"},
            onclick:function(event) { schedulePeriodically(event.data.obj, 20); }
        }
    ]
}

function finishOrder(order) {
    $.ajax({
        url: "orders/"+order.id+"/finish",
        method: "POST",
        data: {},
        dataType: "json"
    })
    .done(ENTITIES['order'].reloadFunction)
    .fail(function(json){alert('Failed: '+JSON.stringify(json))});
}

function createOrder(user) {
    $.ajax({
        url: "orders?userId="+user.id,
        method: "POST",
        contentType: "application/json",
        data: [],
        dataType: "json"
    })
    .done(ENTITIES['order'].reloadFunction)
    .fail(function(json){alert('Failed: '+JSON.stringify(json))});
}

function addProductsToOrder(order, productId, quantity) {
    $.ajax({
        url: "orders/"+order.id+"/addProducts?productId="+productId+"&quantity="+quantity,
        method: "POST",
        contentType: "application/json",
        data: [],
        dataType: "json"
    })
    .done(ENTITIES['order'].reloadFunction)
    .fail(function(json){alert('Failed: '+JSON.stringify(json))});
}

function scheduleOnce(order, delay) {
    $.ajax({
        url: "orders/"+order.id+"/schedule?periodical=false&delay="+delay,
        method: "POST",
        contentType: "application/json",
        data: [],
        dataType: "json"
    })
    .done(ENTITIES['order'].reloadFunction)
    .fail(function(json){alert('Failed: '+JSON.stringify(json))});
}

function schedulePeriodically(order, delay) {
    $.ajax({
        url: "orders/"+order.id+"/schedule?periodical=true&delay="+delay,
        method: "POST",
        contentType: "application/json",
        data: [],
        dataType: "json"
    })
    .done(ENTITIES['order'].reloadFunction)
    .fail(function(json){alert('Failed: '+JSON.stringify(json))});
}
