const inputs = document.querySelectorAll(".input");

function addcl(){
	let parent = this.parentNode.parentNode;
	parent.classList.add("focus");
}

function remcl(){
	let parent = this.parentNode.parentNode;
	if(this.value == ""){
		parent.classList.remove("focus");
	}
}


inputs.forEach(input => {
	input.addEventListener("focus", addcl);
	input.addEventListener("blur", remcl);
});/**
 * 
 */
 function checkpasswordMatch(fieldconfirmpassword){
if(fieldconfirmpassword.value!=$("#password-field ,#password").val()){
fieldconfirmpassword.setCustomValidity("password do not match");
}
else{
fieldconfirmpassword.setCustomValidity("");
}
}

setTimeout(function() {
    $('#message1,#message2').fadeOut('fast');
},3000);


function verifyUser(UId){
	swal({
  title: "Are you sure?",
  text: "you want to verfiry this contact",
  icon: "warning",
  buttons: true,
  dangerMode: true,
})
.then((willDelete) => {
  if (willDelete) {
    window.location="/admin/verify/"+UId
    
  } else {
    swal("User is not verfied!");
  }
});
}

function deleteUser(UId){
	swal({
  title: "Are you sure?",
  text: "you want to delet this user",
  icon: "warning",
  buttons: true,
  dangerMode: true,
})
.then((willDelete) => {
  if (willDelete) {
    window.location="/admin/user/delete/"+UId
    
  } else {
    swal("User is safe!");
  }
});
}


function deleteEvent(evid){
	swal({
  title: "Are you sure?",
  text: "you want to delet this event",
  icon: "warning",
  buttons: true,
  dangerMode: true,
})
.then((willDelete) => {
  if (willDelete) {
    window.location="/admin/deleteevent/"+evid
    
  } else {
    swal("event not deleted!");
  }
});
}
 $(function() {
            $( "#year" ).datepicker({dateFormat: 'yy'});
         });
         
         
         
  $('#datetime').datetimepicker({
            format: 'hh:mm:ss a'
        });       
         
         
         
         
         
         
         $(document).ready(function () {
            $('#sidebarCollapse').on('click', function () {
                $('#sidebar').toggleClass('active');
            });
        }); 
         
         
         
         
         
         
         