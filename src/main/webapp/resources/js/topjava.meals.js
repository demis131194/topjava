$(document).ready(function () {
    makeEditable({
        ajaxUrl: "profile/meals/",
        datatableApi: $("#datatable").DataTable({
            "paging": false,
            "autoWidth": true,
            "info": true,
            "scrollY": true,
            "columns": [
                {
                    "data": "dateTime",
                },
                {
                    "data": "description",
                },
                {
                    "data": "calories",
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false,
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                },
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    })
});