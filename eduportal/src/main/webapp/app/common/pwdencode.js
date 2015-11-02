document.addEventListener( "plusready",  function()
{
    var _BARCODE = 'plugintest',
		B = window.plus.bridge;
    var plugintest = 
    {
        aesEncode : function (Argus) 
        {                                	
            return B.execSync(_BARCODE, "aesEncode", [Argus]);
        }
    };
    window.plus.plugintest = plugintest;
}, true );

function aesEncode(pwdstr) {
    return plus.plugintest.aesEncode(pwdstr);
}