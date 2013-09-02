/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


var rootURL = "http://localhost:8080/SearchForm/resources/projects/";
var page=0;



function getData(){
    getOperatingSystems();
    getLicenses();
    getPL();
    search(1);
}

function getOperatingSystems() {
    console.log('getoss');
        
    $.ajax({
        type: 'GET',
        url: rootURL+'oss',
        dataType: "json", // data type of response
        success: renderOSList
    });
}

function renderOSList(data) {
   
    // JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
    var list = data == null ? [] : (data instanceof Array ? data : [data]);
    //$('#selectOS option').remove();    
	
    $.each(list, function(index, os) {
        $('#selectOS')
        .append($('<option>', {
            value : os
        })
        .text(os)); 
    });
}


function getLicenses() {
    console.log('getLicenses');
        
    $.ajax({
        type: 'GET',
        url: rootURL+'licenses',
        dataType: "json", // data type of response
        success: renderLicensesList
    });
}

function renderLicensesList(data) {
   
    // JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
    var list = data == null ? [] : (data instanceof Array ? data : [data]);
    //$('#selectLic option').remove();    
	
    $.each(list, function(index, lic) {
        $('#selectLic')
        .append($('<option>', {
            value : lic
        })
        .text(lic)); 
    });
}

function getPL() {
    console.log('getProgrammingLanguages');
        
    $.ajax({
        type: 'GET',
        url: rootURL+'languages',
        dataType: "json", // data type of response
        success: renderPLList
    });
}

function renderPLList(data) {
   
    // JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
    var list = data == null ? [] : (data instanceof Array ? data : [data]);
    //$('#selectPL option').remove();    
	
    $.each(list, function(index, lang) {
        $('#selectPL')
        .append($('<option>', {
            value : lang
        })
        .text(lang)); 
    });
}

//function getTags() {
//    console.log('getoss');
//        
//    $.ajax({
//        type: 'GET',
//        url: rootURL+'tags',
//        dataType: "json", // data type of response
//        success: renderTagList
//    });
//}
//
function renderTagList(data) {
   
    // JAX-RS serializes an empty list as null, and a 'collection of one' as an object (not an 'array of one')
    var list = data == null ? [] : (data instanceof Array ? data : [data]);
    //$('#selectOS option').remove();    
	
    $.each(list, function(index, tag) {
        $('#selectTag')
        .append($('<option>', {
            value : tag
        })
        .text(tag)); 
    });
}

function search(page1) {
    page = page1;
   
    $.ajax({
        type: 'POST',
        contentType: 'application/json',
        url: rootURL+'projsearch',
        dataType: "json",
        data: formToJSON(page1),
        success: showProjects,
        error: function(jqXHR, textStatus, errorThrown){
            alert('Greska: ' + textStatus+"\r\n Nije uspelo dovlačenje podataka sa servera. Proverite da li je server pokrenut.");
        }
    });
    return false;
}

function formToJSON(page1){
    var os = $('#selectOS option:selected').text();
    if (os=='Any') os = null;
    var progrlang = $('#selectPL option:selected').text();
    if (progrlang == 'Any') progrlang=null;
    var license = $('#selectLic option:selected').text();
    if (license == 'Any') license = null;
    var tag = $('#txtTag').val();
    if (tag=='') tag = null;
    var keyword = $('#txtKeyword').val();
    if (keyword=='') keyword = null;
    
    
    return JSON.stringify({
        "operatingSystem": os, 
        "license": license, 
        "progrlang": progrlang,
        "tag": tag,
        "keyword": keyword,
        "page": page
    });
}

function showProjects(data){
    var list = data == null ? [] : (data instanceof Array ? data : [data]);
    var i=0;
    //$('#selectLic option').remove();    
    var projectDiv = "";
    $.each(list, function(index, proj) {
        i++;
        projectDiv = projectDiv+"<div id=\""+proj.uri+"\" class=\"projectdiv\">\n\
<table class=\"projectTable\"><tbody><tr><td width=\"80%\"><h1><a href=\""+proj.seeAlso+"\">"+proj.name+"</a></h1></td><td>\n\
    Created by:<h2><a href=\""+proj.maintainer.seeAlso+"\">"+proj.maintainer.name+"</a></h2></td></tr>\n\
 <tr><td><a href=\""+proj.homepage+"\"><h5>Project Home</a>&nbsp&nbsp<a href=\""+proj.downloadpage+"\">Download</h5></a></td></tr>\n\
<tr ><td colspan=\"2\"><h3>Description:</h3>"+proj.description+"</td></tr>\n\
<tr><td colspan=\"2\"><table><tr><td><b>Tags:</b></td><td>"+returnTags(proj.category, 'Tag')+"</td></tr></table></tr>\n\
<tr><td colspan=\"2\"><table><tr><td><b>OS:</b></td><td>"+returnTags(proj.os, 'OS')+"</td></tr></table></tr>\n\
<tr><td colspan=\"2\"><table><tr><td><b>Licenses:</b></td><td>"+returnTags(proj.license, 'Lic')+"</td></tr></table></tr>\n\
<tr><td colspan=\"2\"><table><tr><td><b>Programming language:</b></td><td>"+returnTags(proj.programminglanguage, 'PL')+"</td></tr></table>\n\
</tr></tbody></table><div class=\"divider\"><input type=\"button\" value=\"Show releases\" \n\
onclick=\"showDiv('rel"+proj.uri.substr(34, proj.uri.length)+"')\" class=\"classname\"/>\n\
<div id=\"rel"+proj.uri.substr(34, proj.uri.length)+"\" class=\"projectdiv\"\n\
style=\"display:none\">"+returnReleases(proj.release)+"<input type=\"button\" value=\"Hide releases\" \n\
onclick=\"hideDiv('rel"+proj.uri.substr(34, proj.uri.length)+"')\" class=\"classname\"></div></div></div>";
                
    });
   
    
    projectDiv=projectDiv+"<input type=\"button\" id=\"btnContinue\" name=\"btnCont\" \n\
value=\"Previous page\"";
    if (page==1)  projectDiv=projectDiv+"disabled=\"disabled\"";
    projectDiv=projectDiv+"onclick=\"search("+(page-1)+")\" class=\"classname\"/>\n\
    <span id=\"pageid\">Page "+page+"</span>\n\
<input type=\"button\" id=\"btnContinue\" name=\"btnCont\" value=\"Next page\"";
    if (i<10)
        projectDiv=projectDiv+"disabled=\"disabled\"";
    projectDiv=projectDiv+"onclick=\"search("+(page+1)+")\" class=\"classname\"/>";
    
   
    $('#searchResultDiv').html(projectDiv);
}

function returnTags(proj, type){
    var listTags = proj == null ? [] : (proj instanceof Array ? proj : [proj]);
    //$('#selectLic option').remove();    
    var tags = "";
    $.each(listTags, function(index, tag) {
        tags = tags+"<div class=\"classname\" style=\"color:#FFFFFF\" onclick=\"searchTags('"+tag+"', '"+type+"')\">"+tag+"</div>";
                
    });
    return tags;
    
}

function returnReleases(proj){
    var listRel = proj == null ? [] : (proj instanceof Array ? proj : [proj]);
    var releaseDivs = "";
    
    $.each(listRel, function(index, release) {
        var date = new Date(release.date);
        
        
        releaseDivs = releaseDivs+"<div id=\"release"+release.uri+"\" class=\"releasediv\"><table>\n\
<tr><td><h4><a href=\""+release.seeAlso+"\">Version: "+release.revision+"</a></h4>\n\
</td><td align=\"right\">Date: "+date.toLocaleDateString()+" "+date.toLocaleTimeString()+"</td></tr>\n\
<tr><td colspan=\"2\">"+release.description+"</td></tr></table></div>";
                
    });
    
    return releaseDivs;
    
}

function showDiv(id){
    $('#'+id).fadeIn(300);
}

function hideDiv(id){
    $('#'+id).fadeOut(300);
}

function searchTags(value, type){
    if (type!='Tag')
    $("#select"+type).val(value);
else
    $('#txtTag').val(value);
    search(1);
}

function resetForm(){
    $("select").val(-1);
    $('#txtKeyword').val("");
    $('#txtTag').val("");
    search(1);
}


