$(document).ready( function() {

  $("#showAddBeer").click( function() {
    $("#addBeerCard").show();
    $("#addBeerCardOpen").click();
  });

  $('#deleteBeerModal').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget) 
    var beerId = button.data('id')
    var beerName = button.data('name') 
    var modal = $(this)
    modal.find('#showBeerName').text(beerName)
    modal.find('#deleteBeerIdInput').val(beerId)
  })

  $("#addBeerBtn").click( function (){

    // Upload Tap List Image
    $.ajax({
      url: '/generate_upload_url',
      async: false,
      success: function(data) {
        ajaxUpload(data, '#addBeerTapListImageForm', '#addBeerTapListImage', 'add');
      }
    });

    // Upload Description Image
    $.ajax({
      url: '/generate_upload_url',
      async: false,
      success: function(data) {
        ajaxUpload(data, '#addBeerDescriptionImageForm', '#addBeerDescriptionImage', 'add');
      }
    });

  });

  $(".updateBeerBtn").click( function (){
    var beerId = $(this).attr("data");

    // Upload Tap List Image on Update
    if($("#updateBeerTapListImageForm-"+beerId+" .box").hasClass("updated")){
      $.ajax({
        url: '/generate_upload_url',
        async: false,
        success: function(data) {
          ajaxUpload(data, '#updateBeerTapListImageForm-'+beerId, '#updateBeerTapListImage-'+beerId, 'update', beerId);
        }
      });
    }

    // Upload Description Image on Update
    if($("#updateBeerDescriptionImageForm-"+beerId+" .box").hasClass("updated")){
      $.ajax({
        url: '/generate_upload_url',
        async: false,
        success: function(data) {
          ajaxUpload(data, '#updateBeerDescriptionImageForm-'+beerId, '#updateBeerDescriptionImage-'+beerId, 'update', beerId);
        }
      });
    }

    if (! $("#updateBeerTapListImageForm-"+beerId+" .box").hasClass("updated") && ! $("#updateBeerDescriptionImageForm-"+beerId+" .box").hasClass("updated")  ) {
      $("#updateBeerForm-"+beerId).submit();
    }

  });
    
});

function clearImageInput(input){
  $(input).val("");
}

function ajaxUpload(url, form, input, formAction, id=""){
  $(form).attr("action", url);
  $.ajax({
    type: "POST",
    url: url,
    // Form data
    data: new FormData($(form)[0]),

    // Tell jQuery not to process data or worry about content-type
    // You *must* include these options!
    cache: false,
    contentType: false,
    processData: false,

    // Custom XMLHttpRequest
    xhr: function() {
        var myXhr = $.ajaxSettings.xhr();
        if (myXhr.upload) {
            // For handling the progress of the upload
            myXhr.upload.addEventListener('progress', function(e) {
                if (e.lengthComputable) {
                    $('progress').attr({
                        value: e.loaded,
                        max: e.total,
                    });
                }
            } , false);
        }
        return myXhr;
    },
    success: function(data){
      $(input).val(data) 
      postUploadCallback(formAction, id);
    }
  });

}

function postUploadCallback(formAction, id=""){
  if(formAction == "add"){
    var addBeerTapListImage = $("#addBeerTapListImage").val();
    var addBeerDescriptionImage = $("#addBeerDescriptionImage").val();
    if(addBeerTapListImage != "" && addBeerDescriptionImage != "")
      $("#addBeerForm").submit();
  }

  else if(formAction == "update"){
    var updateBeerTapListImage = $("#updateBeerTapListImage-"+id).val();
    var updateBeerDescriptionImage = $("#updateBeerDescriptionImage-"+id).val();
    if(updateBeerTapListImage != "" && updateBeerDescriptionImage != "")
    $("#updateBeerForm-"+id).submit();
  }
}

function initImageUpload(box) {
  let uploadField = box.querySelector('.image-upload');

  uploadField.addEventListener('change', getFile);

  function getFile(e){
    let file = e.currentTarget.files[0];
    checkType(file);
  }
  
  function previewImage(file){
    let thumb = box.querySelector('.js--image-preview'),
        reader = new FileReader();

    reader.onload = function() {
      thumb.style.backgroundImage = 'url(' + reader.result + ')';
    }
    reader.readAsDataURL(file);
    thumb.className += ' js--no-default';
    box.className += ' updated';
  }

  function checkType(file){
    let imageType = /image.*/;
    if (!file.type.match(imageType)) {
      throw 'Datei ist kein Bild';
    } else if (!file){
      throw 'Kein Bild gewählt';
    } else {
      previewImage(file);
    }
  }
  
}

// initialize box-scope
var boxes = document.querySelectorAll('.box');

for(let i = 0; i < boxes.length; i++) {if (window.CP.shouldStopExecution(1)){break;}
  let box = boxes[i];
  initDropEffect(box);
  initImageUpload(box);
}
window.CP.exitedLoop(1);




/// drop-effect
function initDropEffect(box){
  let area, drop, areaWidth, areaHeight, maxDistance, dropWidth, dropHeight, x, y;
  
  // get clickable area for drop effect
  area = box.querySelector('.js--image-preview');
  area.addEventListener('click', fireRipple);
  
  function fireRipple(e){
    area = e.currentTarget
    // create drop
    if(!drop){
      drop = document.createElement('span');
      drop.className = 'drop';
      this.appendChild(drop);
    }
    // reset animate class
    drop.className = 'drop';
    
    // calculate dimensions of area (longest side)
    areaWidth = getComputedStyle(this, null).getPropertyValue("width");
    areaHeight = getComputedStyle(this, null).getPropertyValue("height");
    maxDistance = Math.max(parseInt(areaWidth, 10), parseInt(areaHeight, 10));

    // set drop dimensions to fill area
    drop.style.width = maxDistance + 'px';
    drop.style.height = maxDistance + 'px';
    
    // calculate dimensions of drop
    dropWidth = getComputedStyle(this, null).getPropertyValue("width");
    dropHeight = getComputedStyle(this, null).getPropertyValue("height");
    
    // calculate relative coordinates of click
    // logic: click coordinates relative to page - parent's position relative to page - half of self height/width to make it controllable from the center
    x = e.pageX - this.offsetLeft - (parseInt(dropWidth, 10)/2);
    y = e.pageY - this.offsetTop - (parseInt(dropHeight, 10)/2) - 30;
    
    // position drop and animate
    drop.style.top = y + 'px';
    drop.style.left = x + 'px';
    drop.className += ' animate';
    e.stopPropagation();
    
  }
}
