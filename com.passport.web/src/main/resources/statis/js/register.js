(function () {
    FastClick.attach(document.body);

    var remained;

    $('.j-btn-validate').click(function () {
        if (!$(this).hasClass('disabled')) {
            sendCode($(this));
        }
    });
    remained = getCookieValue('secondsremained');
    if (remained > 0) {
        setTime($('.j-btn-validate'));
    }

    $.get("/ajax/SpreadRegister.ashx?OperateType=2&Ran=" + Math.random(), function (resonseText) {

        var data = eval("(" + resonseText + ")");
        //console.log(data);
        if (data.ErrorNum === "-1") {
            $("#signUp").html('<p style="font-size: 1.4em;color:  #fff;text-align:  center;margin-top: 50%;">' + data.ErrorDescribe + '</p>');
        }
    });

    $('.j-btn-confirm').click(function () {
        var phone = $('#inputPhone').val(),
            code = $('#inputCode').val(),
            password = $('#inputPassword').val(),
            rePassword = $('#inputRePassword').val();

        if (phone == '') {
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
            $.toast('两次密码不一致', 'cancel');
            return false;
        } else {
            submitForm(phone, code, password);
        }
    });

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
    //发送验证码
    function sendCode(obj) {
        var phone = $('#inputPhone').val();
        var result = isPhoneNum(phone);
        if (result) {
            $.ajax({
                type: 'POST',
                url: '/ajax/SpreadRegister.ashx?Ran' + Math.random(),
                data: { OperateType: 0, Accounts: phone },
                dataType: 'text',
                success: function (data) {
                    var dataJson = eval('(' + data + ')');
                    console.log(dataJson);
                    if (dataJson.success === '0') {
                        $.toast(dataJson.content, 'success');
                    } else {
                        $.toast(dataJson.content, 'cancel');
                    }
                },
                error: function (responseText) {
                    console.log(responseText);
                }
            });
            addCookie('secondsremained', 60, 60);
            setTime(obj);
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
    function submitForm(phone, code, password) {
        var encrypt = new JSEncrypt();
        encrypt.setPublicKey($('#pubkey').val());
        var encrypted = encrypt.encrypt(password);

        var GameID = getQueryString("GameID");

        if (GameID) {
            $.ajax({
                type: 'POST',
                url: '/ajax/SpreadRegister.ashx',
                data: { OperateType: 1, Accounts: phone, Code: code, LoginPass: encodeURI(encrypted).replace(/\+/g, '%2B'), GameID: GameID },
                dataType: 'text',
                success: function (data) {
                    var dataJson = eval('(' + data + ')');
                    console.log(dataJson);
                    if (dataJson.ErrorNum === '0') {
                        // $('#mask').hide();
                        // $.toast(dataJson.ErrorDescribe, 'success');
                        // window.location.href = dataJson.DownUrl;
                        document.getElementById("regAccount").innerText = phone;
                        $("#cover").show();
                    } else {
                        $.toast(dataJson.ErrorDescribe, 'cancel');
                    }
                },
                error: function (responseText) {
                    console.log(responseText);
                }
            });
        } else {
            alert("参数错误");
        }
    }

    function getQueryString(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return decodeURI(r[2]); return null;
    }

})();
