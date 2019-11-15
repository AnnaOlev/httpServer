let e = document.getElementById('forma');
if (e) {
  e.addEventListener('submit', function(e) {
    e.preventDefault();
    let data = document.getElementById('numbers').value;
    if (data) {
        var xhr = new XMLHttpRequest();
        var result = 'если это тут, то все плохо';
        xhr.open("POST", `/${data}`, true);
        xhr.send(data);
        xhr.onreadystatechange = function() {
            if(xhr.readyState == 4 && xhr.status == 200) {
                result = xhr.responseText
                document.getElementById('result').value = result
            }
         }
     }
})
}
 function checkInputNumbers(input) {
 var onlyNums = /^[0-9.0-9,]*$/.test(input.value);
 if (onlyNums)
    return true;
    else
       {
         alert("Неверный ввод. Допустимы только числа через запятую");
         var str = input.value;
         str = str.substring(0, str.length - 1);
         input.value = str;
       }
 }
