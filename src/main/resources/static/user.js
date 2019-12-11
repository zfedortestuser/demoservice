ENTITIES['user'] = {
    name: 'Пользователи',
    url: 'users',
    reloadFunction: null,
    rows: [],
    actions:[
        {
            name:'Создать заказ',
            isVisible:function(order) {return true},
            onclick:function(event) { createOrder(event.data.obj); }
        }
    ]
}