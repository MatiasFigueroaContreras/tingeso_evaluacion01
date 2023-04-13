// Recibe el numero del mes (0-11) y lo transforma a texto
function getMonthName(month_number) {
    const date = new Date();
    date.setDate(1); // Para prevenir error con meses
    date.setMonth(month_number);
    return date.toLocaleString("es-US", { month: "long" });
}

// Select estandar para los años anteriores al actual
function getStandardMonthSelect() {
    var select = document.createElement("select");
    select.name = "mes";
    select.id = "mes";
    select.required = true;
    var option = document.createElement("option");
    option.value = "";
    option.text = "Mes";
    option.disabled = true;
    option.selected = true;
    select.append(option);
    for (let month = 0; month < 12; month++) {
        var option = document.createElement("option");
        option.value = month + 1;
        option.text = getMonthName(month);
        select.append(option);
    }
    return select;
}

function getStandardFortnightSelect() {
    var select = document.createElement("select");
    select.name = "quincena";
    select.id = "quincena";
    select.required = true;
    var option = document.createElement("option");
    option.value = "";
    option.text = "Quincena";
    option.disabled = true;
    option.selected = true;
    select.append(option);
    ["Primera", "Segunda"].forEach((el, i) => {
        var option = document.createElement("option");
        option.value = i + 1;
        option.text = el;
        select.append(option);
    });
    return select;
}

// Para la seleccion de la quincena
const year_select = document.getElementById("year");
const month_select = document.getElementById("mes");
const fortnight_select = document.getElementById("quincena");
const start_year = 2021;
const current_date = new Date();
const current_year = current_date.getFullYear();
const current_month = current_date.getMonth() + 1;
const current_day = current_date.getDate();
const fortnights = ["Primera", "Segunda"];
const standard_fortnights_select = getStandardFortnightSelect();
const standard_month_select = getStandardMonthSelect();

// Se calcula el mes y la quincena al cual llega segun
//  el dia actual (tomando 2 dias de margen)
var final_fortnight;
var final_month;
if (current_day >= 27) {
    final_month = current_month;
    final_fortnight = 2;
} else if (current_day >= 13) {
    final_month = current_month;
    final_fortnight = 1;
} else {
    final_month = current_month - 1;
    final_fortnight = 2;
}

for (let fortnight = 1; fortnight <= final_fortnight; fortnight++) {
    var fortnight_option = document.createElement("option");
    fortnight_option.value = fortnight;
    fortnight_option.text = fortnights[fortnight - 1];
    fortnight_select.append(fortnight_option);
}

// Valor actual de la quincena por defecto
fortnight_select.value = final_fortnight;

// Meses que se pueden seleccionar
for (let month = 1; month <= final_month; month++) {
    var month_option = document.createElement("option");
    month_option.value = month;
    month_option.text = getMonthName(month - 1);
    month_select.append(month_option);
}

var final_year = current_year;
if (final_month === -1) {
    // No se alcanza a pasar al siguiente año por la quincena
    //  por lo tanto se llega hasta el anterior
    month_select.replaceWith(standard_month_select);
    final_year = current_year - 1;
    month_select.value = 12;
} else {
    // Valor actual del mes por defecto
    month_select.value = final_month;
}

// Años que puede seleccionar
for (let year = current_year; start_year <= year; year--) {
    var year_option = document.createElement("option");
    year_option.value = year;
    year_option.text = year;
    year_select.append(year_option);
}

// Valor actual del año por defecto
year_select.value = current_year;

// Cambiar meses disponibles en funcion del año
year_select.addEventListener("change", (e) => {
    const year_selected = e.target.value;
    const current_month_select = document.getElementById("mes");
    if (year_selected == final_year) {
        month_select.value = current_month_select.value;
        month_select.value = month_select.value ?? "";
        current_month_select.replaceWith(month_select);
    } else {
        standard_month_select.value = current_month_select.value;
        current_month_select.replaceWith(standard_month_select);
    }
});

// Cambiar quincenas disponibles en funcion del mes
month_select.addEventListener("change", (e) => {
    const month_selected = e.target.value;
    const current_fortnight_select = document.getElementById("quincena");
    if (month_selected == final_month) {
        fortnight_select.value = current_fortnight_select.value;
        fortnight_select.value = fortnight_select.value ?? "";
        current_fortnight_select.replaceWith(fortnight_select);
    } else {
        standard_fortnights_select.value = current_fortnight_select.value;
        current_fortnight_select.replaceWith(standard_fortnights_select);
    }
});
