$(document).ready(function(){
	
	$("#orders_dataTable").DataTable({
		"order": [[ 0, "desc" ]],
		"columns": [
			{"name": "date", "orderable": true},
			{"name": "customer", "orderable": true},
			{"name": "details", "orderable": false},
			{"name": "invoice", "orderable": false},
			{"name": "reward", "orderable": false},
			{"name": "discount", "orderable": false},
			{"name": "total", "orderable": true}
		]
	});
});