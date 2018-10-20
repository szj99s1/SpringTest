<#macro js path>
	<#assign fix=(path?index_of("?")==-1)?string("?","&")/>
    <script type="text/javascript" src="${url(path+fix+"_stmp="+_timestamp,false)}"></script>
</#macro>

<#macro css path>
	<#assign fix=(path?index_of("?")==-1)?string("?","&")/>
    <link type="text/css" rel="stylesheet" href="${url(path+fix+"_stmp="+_timestamp,false)}"/>
</#macro>

<#macro cleanHeader title="舜联PAY">
<!doctype html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>${title}</title>
    <meta name="keywords" content="舜联PAY" />
    <meta name="description" content="舜联PAY" />
<#nested>
</head>
</#macro>

<#macro htmlHeader title="舜联PAY" showRecords=false>
<@cleanHeader title=title>
    <@css path="/css/master.css"/>
    <script type="text/javascript" src="${url("/js/jquery-1.11.1.min.js",false)}"></script>
    <script type="text/javascript" src="${url("/js/jquery.form.js",false)}"></script>
    <script type="text/javascript" src="${url("/js/jquery.cookie.js",false)}"></script>
    <@js path="/js/common.js"/>
    <@js path="/js/validator.js"/>
    <script type="text/javascript">
    var _imgPath='${springMacroRequestContext.getContextUrl("/images/")}';
    _contextPath='${springMacroRequestContext.getContextUrl("/")}';
    </script>
    <#if showRecords><@js path="/js/jquery.record.js?_smtp=20150112"/></#if>
    <#nested>
</@cleanHeader>
</#macro>

<#macro htmlBody>
<body>
<#nested>
</#macro>

<#macro htmlFooter showRecords=false>
<#if showRecords><@js path="/js/jquery.record.js"/></#if>
<#nested>
</body>
</html>
</#macro>

<#macro form action method="post"  id="_form" onsubmit="return $form.submit(this);" show=true defaultButton=true target="_self" enctype="application/x-www-form-urlencoded">
<form action="${url(action,false)}" method="${method}" id="${id}" target="${target}" onsubmit="${onsubmit}"<#if !show> style="display:none"</#if> enctype="${enctype}">
    <#if _token?exists><input value="${_token}" name="_token" type="hidden"/></#if>
    <#nested>
    <#if defaultButton>
    <div class="all_space"></div>
    <div class="all_inbz4"  style="margin-top:15px">
        <span class="anniu"><a href="javascript:void(0);" onclick="$('#${id}').submit()">提交</a> </span><span class="anniu"><a href="javascript:void(0);" onclick="$form.reset('#${id}')">重置</a> </span>
    </div>
    </#if>
</form>
</#macro>

<#macro page>
<@htmlHeader>
    <script type="text/javascript" src="${url("/js/My97DatePicker/WdatePicker.js",false)}"></script>
</@htmlHeader>
<@htmlBody>
<#nested>
</@htmlBody>
</#macro>

<#macro showTitle title>
<div class="main">
    <div class="title">${title}</div>
    <#nested>
</div>
</#macro>

<#macro showContent>
<div class="area">
<#nested>
</div>
</#macro>

<#macro recordset header  id="_record" showFooter=true>
    <#assign keys=header?keys>
    <div class="all_tab">
    <table width="100%" border="0" bgcolor="#ffffff" id="${id}" style="display:none">
        <thead>
            <tr>
                <#list keys as key><th<#if header[key]!=""> width="${header[key]}"</#if> align="center" bgcolor="#D8D8D8" style="padding:3px">${key}</th></#list>
            </tr>
        </thead>
        <tbody>
        </tbody>
        <#if showFooter>
        <tfoot>
        <tr><td bgcolor="#ECECEC" colspan="${keys?size}"></td></tr>
        </tfoot>
        </#if>
    </table>
    </div>
</#macro>