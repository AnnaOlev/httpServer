
function addRow(id){
    var namefield = document.getElementById('name');
    var breedfield = document.getElementById('breed');
    var sexfield = document.getElementById('sex');
    var agefield = document.getElementById('age');
    var name = namefield.value;
    var breed = breedfield.value;
    var sex = sexfield.value;
    var age = agefield.value
    if (name===''||breed===''||sex===''||age==='')
        return null;
    var tbody = document.getElementById(id).getElementsByTagName("TBODY")[0];
    var row = document.createElement("TR")
    var td1 = document.createElement("TD")
    td1.appendChild(document.createTextNode(namefield.value))
    var td2 = document.createElement("TD")
    td2.appendChild (document.createTextNode(breedfield.value))
    var td3 = document.createElement("TD")
    td3.appendChild(document.createTextNode(sexfield.value))
    var td4 = document.createElement("TD")
    td4.appendChild (document.createTextNode(agefield.value))
    var td5 = document.createElement("TD")
    var input = document.createElement("input");
    input.type = "button";
    input.value = "Удалить";
    input.onclick = function() {
        var btn = this
        var row = btn.parentNode.parentNode;
        var id = row.rowIndex;
        row.parentNode.removeChild(row);
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
    td5.appendChild(input)
    var td6 = document.createElement("TD")
    var input = document.createElement("input");
    input.type = "button";
    input.value = "Отредактировать";
    input.onclick = function(){
           var btn = this
           var row = btn.parentNode.parentNode;
           var id = row.rowIndex;
           document.getElementById("zatemnenie_change").style.display = 'block'
           var cells = row.getElementsByTagName("td");
           document.getElementById('namenew').value = cells[0].innerHTML
           document.getElementById('breednew').value = cells[1].innerHTML
           document.getElementById('sexnew').value = cells[2].innerHTML
           document.getElementById('agenew').value = cells[3].innerHTML
           document.getElementById("closebuttonnew").onclick = function() {changeRow('table',cells,id);};
    }
    td6.appendChild(input)
    row.appendChild(td1);
    row.appendChild(td2);
    row.appendChild(td3);
    row.appendChild(td4);
    row.appendChild(td5);
    row.appendChild(td6);
    tbody.appendChild(row);

    var xhr = new XMLHttpRequest();
    var result = name+'&'+breed+'&'+sex+'&'+age;
    xhr.open("POST", `/newdog`, true);
    xhr.send(result);
    xhr.onreadystatechange = function() {
            if(xhr.readyState == 4 && xhr.status == 200) {
                result = xhr.responseText
                if (result==='OK')
                   alert('Сервер сообщает: добавление прошло успешно')
            }
         }
    document.getElementById('name').value = ''
    document.getElementById('sex').value = ''
    document.getElementById('breed').value = ''
    document.getElementById('age').value = ''
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

  function changeRow(table, cells,id){
     cells[0].innerHTML = document.getElementById("namenew").value
     cells[1].innerHTML = document.getElementById("breednew").value
     cells[2].innerHTML = document.getElementById("sexnew").value
     cells[3].innerHTML = document.getElementById("agenew").value
     var xhr = new XMLHttpRequest();
     var newid = id-1;
     var result = newid +'&' + cells[0].innerHTML+'&'+ cells[1].innerHTML+'&'+ cells[2].innerHTML+'&'+ cells[3].innerHTML;
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
