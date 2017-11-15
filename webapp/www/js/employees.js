$(document).ready(function(){
	
	$("#dataTable").DataTable({
		"columns": [
			{"name": "name", "orderable": true},
			{"name": "phone", "orderable": true},
			{"name": "carrier", "orderable": false},
			{"name": "bartending", "orderable": true},
			{"name": "update", "orderable": false}
		]
	});

	$('#addEmployeeModal').on('show.bs.modal', function (event) {
	    var button = $(event.relatedTarget);
	    var employeeId = button.data('id');
	    var employeeName = button.data('name'); 
	    var employeePhone = button.data('phone');
	    var employeeCarrier = button.data('carrier');
	    var employeeBartending = button.data('bartending');
	    var modalLabel = button.data('label');
	    var btnText = button.data('btn');
	    var formAction = button.data('action');
	    var modal = $(this);
	    modal.find('#addEmployeeModalLabel').text(modalLabel);
	    modal.find('#addEmployeeBtn').text(btnText);
	    modal.find('#addEmployeeName').val(employeeName);
	    modal.find('#addEmployeeId').val(employeeId);
	    modal.find('#addEmployeePhone').val(employeePhone);
	    modal.find('#addEmployeeCarrier').val(employeeCarrier); 
	    if(employeeBartending == "True")
	    	modal.find('#addEmployeeBartending').attr("checked");
	    modal.find('#addEmployeeAction').val(formAction);
	})
	$('#deleteEmployeeModal').on('show.bs.modal', function (event) {
	    var button = $(event.relatedTarget);
	    var employeeId = button.data('id');
	    var employeeName = button.data('name'); 
	    var modal = $(this);
	    modal.find('#showEmployeeName').text(employeeName);
	    modal.find('#deleteEmployeeIdInput').val(employeeId);
	})
});