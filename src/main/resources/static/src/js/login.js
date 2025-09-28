
(function ($) {
    "use strict";


    /*==================================================================
    [ Focus Contact2 ]*/
    $('.input100').each(function(){
        $(this).on('blur', function(){
            if($(this).val().trim() != "") {
                $(this).addClass('has-val');
            }
            else {
                $(this).removeClass('has-val');
            }
        })    
    })
  
  
    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');

    $('.validate-form').on('submit',function(){
        var check = true;

        for(var i=0; i<input.length; i++) {
            if(validate(input[i]) == false){
                showValidate(input[i]);
                check=false;
            }
        }

        return check;
    });


    $('.validate-form .input100').each(function(){
        $(this).focus(function(){
           hideValidate(this);
        });
    });

    function validate (input) {
        var inputValue = $(input).val().trim();
        var inputName = $(input).attr('name');
        
        // Check if field is empty
        if(inputValue == '') {
            return false;
        }
        
        // Username validation
        if(inputName == 'username') {
            if(inputValue.length < 3 || inputValue.length > 20) {
                $(input).parent().attr('data-validate', 'Username must be between 3-20 characters');
                return false;
            }
            if(!/^[a-zA-Z0-9_]+$/.test(inputValue)) {
                $(input).parent().attr('data-validate', 'Username can only contain letters, numbers, and underscores');
                return false;
            }
        }
        
        // Password validation
        if(inputName == 'password') {
            if(inputValue.length < 6) {
                $(input).parent().attr('data-validate', 'Password must be at least 6 characters');
                return false;
            }
        }
        
        // Email validation (if applicable)
        if($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
            if(inputValue.match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                $(input).parent().attr('data-validate', 'Please enter a valid email address');
                return false;
            }
        }
        
        return true;
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
    }
    

})(jQuery);