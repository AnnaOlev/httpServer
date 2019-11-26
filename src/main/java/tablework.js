var breedfield;
var namefield;
var agefield;
var sexfield;
var name;
var breed;
var sex;
var age;
var id_dog;

function loader(){
var xhr = new XMLHttpRequest();
 xhr.open("GET", `/givemedog`, true);
    xhr.send();
    xhr.onreadystatechange = function() {
    if(xhr.readyState == 4 && xhr.status == 200) {
       result = xhr.responseText;
       //alert(result)
       var arr = result.split('&')
       var j = 0;
       for (var i = 0; i < arr.length; i++){
                switch(j) {
                case (0):
                    id_dog = arr[i];
                    break;
                case (1):
                    name = arr[i];
                    break;
                case(2):
                    breed = arr[i];
                    break;
                case(3):
                    sex = arr[i];
                    break;
                case(4):
                    age = arr[i];
                    break;
                default:
                    alert( "Нет таких значений" );
                }
                j++;
                if (j==5) {
                    addRow('table', id_dog)
                    j = 0
                }

            }
       }
     }
 }

function preAddRow(id){
    namefield = document.getElementById('name');
    breedfield = document.getElementById('breed');
    sexfield = document.getElementById('sex');
    agefield = document.getElementById('age');
    name = namefield.value;
    breed = breedfield.value;
    sex = sexfield.value;
    age = agefield.value
    if (name===''||breed===''||sex===''||age==='')
        return null;

    var xhr = new XMLHttpRequest();
    var result = name+'&'+breed+'&'+sex+'&'+age;
    xhr.open("POST", `/newdog`, true);
    xhr.send(result);
    xhr.onreadystatechange = function() {
         if(xhr.readyState == 4 && xhr.status == 200) {
              result = xhr.responseText
              //alert(result);
              id_dog = result;
              addRow(id);
             }

         return;
     }
     document.getElementById('name').value = ''
     document.getElementById('sex').value = ''
     document.getElementById('breed').value = ''
     document.getElementById('age').value = ''
}
function addRow(id){

    var tbody = document.getElementById(id).getElementsByTagName("TBODY")[0];
    var row = document.createElement("TR")
    var td1 = document.createElement("TD")
    td1.appendChild(document.createTextNode(id_dog))
    var td2 = document.createElement("TD")
    td2.appendChild(document.createTextNode(name))
    var td3 = document.createElement("TD")
    td3.appendChild (document.createTextNode(breed))
    var td4 = document.createElement("TD")
    td4.appendChild(document.createTextNode(sex))
    var td5 = document.createElement("TD")
    td5.appendChild (document.createTextNode(age))
    var td6 = document.createElement("TD")
    var input = document.createElement("input");
    input.type = "button";
    input.value = "Удалить";
    input.onclick = function() {
        var btn = this
        var row = btn.parentNode.parentNode;
        var cells = row.getElementsByTagName("td");
        var id = cells[0].innerHTML;
        row.parentNode.removeChild(row);
        var xhr = new XMLHttpRequest();
        xhr.open("POST", `/deletedog`, true);
        var result = id;
            xhr.send(result);
            xhr.onreadystatechange = function() {
                    if(xhr.readyState == 4 && xhr.status == 200) {
                        result = xhr.responseText;
                        if (result==='OK')
                            alert('Сервер сообщает: удаление прошло успешно')
                    }
                 }
    };
    td6.appendChild(input)
    var td7 = document.createElement("TD")
    var input = document.createElement("input");
    input.type = "button";
    input.value = "Отредактировать";
    input.onclick = function(){
           var btn = this
           var row = btn.parentNode.parentNode;
           var id = row.rowIndex;
           document.getElementById("zatemnenie_change").style.display = 'block'
           var cells = row.getElementsByTagName("td");
           document.getElementById('namenew').value = cells[1].innerHTML
           document.getElementById('breednew').value = cells[2].innerHTML
           document.getElementById('sexnew').value = cells[3].innerHTML
           document.getElementById('agenew').value = cells[4].innerHTML
           document.getElementById("closebuttonnew").onclick = function() {changeRow('table',cells,id);};
    }
    td7.appendChild(input)
    row.appendChild(td1);
    row.appendChild(td2);
    row.appendChild(td3);
    row.appendChild(td4);
    row.appendChild(td5);
    row.appendChild(td6);
    row.appendChild(td7)
    tbody.appendChild(row);
  }


 function checkLength(len,ele){
   var fieldLength = ele.value.length;
   if(fieldLength <= len){
     return true;
   }
   else
   {
     var str = ele.value;
     str = str.substring(0, str.length - 1);
     ele.value = str;
   }
 }

 function checkInputText(input) {
 var onlyLetters = /^[а-яёА-ЯЁa-zA-Z]*$/.test(input.value);
 if (onlyLetters)
    return true;
    else
       {
         alert("Неверный ввод. Допустимы только буквы");
         var str = input.value;
         str = str.substring(0, str.length - 1);
         input.value = str;
       }
 }

 function checkInputSex(input, len) {
 checkLength(len,input)
  var onlyLetters = /^[fmмж]*$/.test(input.value);
  if (onlyLetters)
     return true;
     else
        {
          alert("Неверный ввод. Допустим формат m/f или м/ж");
          var str = input.value;
          str = str.substring(0, str.length - 1);
          input.value = str;
        }
  }

  function changeRow(table, cells, id){
     cells[1].innerHTML = document.getElementById("namenew").value
     cells[2].innerHTML = document.getElementById("breednew").value
     cells[3].innerHTML = document.getElementById("sexnew").value
     cells[4].innerHTML = document.getElementById("agenew").value
     var xhr = new XMLHttpRequest();
     var result = cells[0].innerHTML+'&'+ cells[1].innerHTML+'&'+ cells[2].innerHTML+'&'+ cells[3].innerHTML + "&" + cells[4].innerHTML;
     xhr.open("POST", `/changedog`, true);
         xhr.send(result);
         xhr.onreadystatechange = function() {
                 if(xhr.readyState == 4 && xhr.status == 200) {
                     result = xhr.responseText
                     if (result==='OK')
                      аlert('Сервер сообщает: редактирование прошло успешно')
                 }
              }
     document.getElementById("zatemnenie_change").style.display = 'none'

  }
