
 _calculateAge = function(birthday) { 
	  function convertStringToDate(fecha){
			var day = fecha.substring(0,2);
			var month = fecha.substring(3,5);
			var year = fecha.substring(6);
			return new Date (year, month-1, day);
		}

	    var d = convertStringToDate(birthday);

	    var ageDifMs = Date.now() - d.getTime();
	    var ageDate = new Date(ageDifMs); // miliseconds from epoch
	    return Math.abs(ageDate.getUTCFullYear() - 1970);
	}


errorData = function(data) {
     return 'Error: ' + data.responseText
 }
   	
 setUserURL = function(userData) {
		var userId = parseInt(userData.id);
	 	if (userData.usersTable_id) {
	 		//Edición usuario
	 		var rowData = jQuery('#usersTable').jqGrid("getRowData", userData.usersTable_id);
	 		userId = rowData.id;
	 	}
		if (isNaN(parseInt(userData.usersTable_id || userData.id))) {
			// Creación usuario
			userId = '';
		}	
		jQuery('#usersTable').jqGrid('setGridParam',{'editurl': '/api/user/' + userId});
	}
	
 checkIdenticalPassword = function(userData) {
		return (userData.password == userData.passwordValidation);
	}
	
 validarDatosUsuario = function(userData) {
		var valido = true;
	    	var mensaje = "";
	    	if ((_calculateAge(userData.birthdate) < 14)) {
	    		valido = false;
	    		mensaje = "Debe ser mayor de 14 años";
	    	} else if (!checkIdenticalPassword(userData)) {
	    		valido = false;
	    		mensaje = "El password no coincide";	
	    	}
	    	return [valido, mensaje];
	}
	
 serializeData =  function(postData) {
		var userId = parseInt(postData.id);
		if (isNaN(userId)) {
			postData.id = '';
		}	
     return JSON.stringify(postData);
	}
	
	
	
var COL_MODEL = [
		{name:'id',index:'id', width:65, hidden: true, editable: false, edittype: "text"},
   		{name:'name',index:'name', width:150, sortable: true, editable: true, edittype: "text", editrules:{required:true}},
   		{name:'surnames',index:'surnames', sortable: true, width:100, editable: true, edittype: "text", editrules:{required:true}},
   		{name:'email',index:'email', sortable: true, width:100, editable: true, edittype: "text", editrules:{required:true, email:true}},
   		{name:'password',index:'password',  width:100,  editable: true, hidden: true, editrules: {edithidden: true, required:true}, edittype: "text"},
   		{name:'passwordValidation',index:'passwordValidation',  width:100,  editable: true, hidden: true, editrules: {edithidden: true, required:true}, edittype: "text"},
   		{name:'birthdate',index:'birthdate', sortable: true, width:100, editable: true, editrules:{required:true}, editoptions:{size:12,
   			dataInit:function(el){
				 $( el).datepicker(
					    	{changeMonth: true,
					        changeYear: true,
					        dateFormat: "dd/mm/yy"
					        }
					    );
			},
			defaultValue: function(){
				var currentTime = new Date();
				var month = parseInt(currentTime.getMonth() + 1);
				month = month <= 9 ? "0"+month : month;
				var day = currentTime.getDate();
				day = day <= 9 ? "0"+day : day;
				var year = currentTime.getFullYear();
				return day + "/" + month + "/" +year;				
			}
		}}
   	];

var GRID_CONFIG = {
	   	url:'/api/user/',
		datatype: "json",
		height: 500,
		width: 1000,
	   	colNames:['ID','Nombre', 'Apellidos', 'Email', 'Password', 'Repetir Password', 'Fecha nacimiento'],
	   	colModel: COL_MODEL,
	   	rowNum:50,
		rowTotal: 2000,
		rowList : [20,30,50],
		loadonce:true,
	   	mtype: "GET",
		rownumbers: true,
		rownumWidth: 40,
		gridview: true,
	   	pager: '#utoolbar',
	   	sortname: 'email',
	    viewrecords: true,
	    sortorder: "asc",
		caption: "Usuarios",
		ignoreCase: true
	
	};
   	
 var ajaxEditOptions = {
    	 headers: {  
	            "accept": "application/json;odata=verbose", //It defines the Data format   
	            "content-type": "application/json;odata=verbose" //It defines the content type as JSON  
	        }
     };  

 var buttonConfig = {
              height: 'auto',
              width: 600,
              recreateForm: true,
              closeAfterEdit: true,
              closeAfterAdd: true,
              ajaxEditOptions: ajaxEditOptions,
              onclickSubmit: setUserURL,
              serializeEditData: serializeData,
              beforeSubmit: function (userData) {
             	 setUserURL(userData);
             	return validarDatosUsuario(userData); 
              },
              mtype: "POST",
              errorTextFormat: errorData,
              afterSubmit: function () {
      		    $(this).jqGrid("setGridParam", {datatype: 'json'});
      		    return [true];
      		}
          };
 