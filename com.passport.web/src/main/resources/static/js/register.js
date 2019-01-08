$(document).ready(function(){
    var $phone='';
    $(".getCode").click(function(){
        if (!$(this).hasClass('disabled')) {
            sendCode($(this));
        }
    });

    $(".register-btn").click(function(){
        var phone = $('#inputPhone').val(),
            code = $('#inputCode').val(),
            password = $('#inputPwd').val(),
            rePassword = $('#inputRepwd').val(),
            inputId = $('#inputId').val();
        inputName = $("#inputName").val();
        if(inputId == ''){
            $('#inputId').val("0");
            return false;
        }
        else if (phone == '') {
            $.toast('请输入手机号', 'cancel');
            return false;
        } else if (!isPhoneNum(phone)) {
            return false;
        } else if (code == '') {
            $.toast('请输入验证码', 'cancel');
            return false;
        } else if (password == '') {
            $.toast('请输入密码', 'cancel');
            return false;
        } else if (!isRightPassword(password)) {
            return false;
        } else if (rePassword == '') {
            $.toast('请输入确认密码', 'cancel');
            return false;
        } else if (rePassword != password) {
            console.log('确认密码：'+rePassword);
            console.log('密码：'+password);
            $.toast('两次密码不一致', 'cancel');
            return false;
        } else {
            submitForm(phone, code, password, inputId);
        }
    })
    //校验手机号是否合法
    function isPhoneNum(phonenum) {
        var phoneReg = /^(((19[0-9]{1})|(13[0-9]{1})|(14[0-9]{1})|(15[0-9]{1})|(17[0-9]{1})|(166)|(18[0-9]{1}))+\d{8})$/;
        if (!phoneReg.test(phonenum)) {
            $.toast('请输入有效的手机号码', 'cancel');
            return false;
        } else {
            return true;
        }
    }
    //校验密码是否合法
    function isRightPassword(password) {
        if (password.length < 6)
        {
            $.toast('密码长度不能小于6位', 'cancel');
            return false;
        }
        var pwdReg = /^(\w){6,}$/;
        if (!pwdReg.test(password)) {
            $.toast('密码不能有特殊字符', 'cancel');
            return false;
        } else {
            return true;
        }
    }
    //发送验证码时添加cookie
    function addCookie(name, value, expiresHours) {
        var cookieString = name + "=" + escape(value);
        if (expiresHours > 0) {
            var date = new Date();
            date.setTime(date.getTime() + expiresHours * 1000);
            cookieString = cookieString + ";expires=" + date.toUTCString();
        }
        document.cookie = cookieString;
    }
    //修改cookie的值
    function editCookie(name, value, expiresHours) {
        var cookieString = name + "=" + escape(value);
        if (expiresHours > 0) {
            var date = new Date();
            date.setTime(date.getTime() + expiresHours * 1000);
            cookieString = cookieString + ";expires=" + date.toGMTString();
        }
        document.cookie = cookieString;
    }
    //根据名字获取cookie的值
    function getCookieValue(name) {
        var strCookie = document.cookie;
        var arrCookie = strCookie.split("; ");
        for (var i = 0; i < arrCookie.length; i++) {
            var arr = arrCookie[i].split("=");
            if (arr[0] == name) {
                return unescape(arr[1]);
                break;
            } else {
                return "";
                break;
            }
        }
    }
    //发送验证码
    var url='/appinterface';
    function sendCode(obj) {
        var phone = $('#inputPhone').val();
        console.log(typeof(phone));
        var result = isPhoneNum(phone);
        if (result) {
            $.ajax({
                type: 'POST',
                url: '/appinterface/regBuildCode',
                data: '{"phoneNo":'+phone+'}',
                headers:{'Content-Type': 'application/json'},
                success: function (data) {
                    if (data.code === '200') {
                        $.toast(data.msg, 'success');
                        addCookie('secondsremained', 60, 60);
                        setTime(obj);
                    } else {
                        $.toast(data.msg, 'cancel');
                    }
                },
                error: function (responseText) {
                    console.log(responseText);
                }
            });
        }
    }
    //开始倒计时
    var countdown;
    function setTime(obj) {
        countdown = getCookieValue('secondsremained');
        if (countdown == 0) {
            obj.removeClass('disabled');
            obj.text('获取验证码');
            return;
        } else {
            obj.addClass('disabled');
            obj.text(countdown + 's后重新获取');
            countdown--;
            editCookie("secondsremained", countdown, countdown + 1);
        }
        setTimeout(function () {
            setTime(obj);
        }, 1000);
    }

    //提交表单信息
    function submitForm(phone, code, password,id) {
        $.ajax({
            type: 'POST',
            url: '/appinterface/regByQr',
            data: '{ "accessName":"'+phone+'","accessToken":"'+password+'","loginType":1'+',"recommendId":"'+id+'","validateCode":"'+code+'"}',
            headers:{'Content-Type': 'application/json','Accept': 'application/json'},
            success: function (data) {
                console.log(data);
                // if (dataJson.ErrorNum === '0') {
                //     $.toast('注册成功', 'cancel');
                // } else {
                //     $.toast(dataJson.ErrorDescribe, 'cancel');
                // }
                if(data.success){
                    // $.toast(data.msg,'cancel');
                    $(".cover").show();
                }else{
                    $.toast(data.msg,'cancel');

                }            },
            error: function (responseText) {
                console.log(responseText);
            }
        });
    }
})