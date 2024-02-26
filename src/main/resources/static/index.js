function calculate() {
    const amount = document.getElementById("amount").value;
    const dayCount = document.getElementById("count").value;
    const percents = document.getElementById("percents").value;

    let result = 0;

    if(amount > 0 && dayCount > 0)
        result = amount * (percents / 100) * (dayCount / 360).toFixed(2);

    document.getElementById("result").innerHTML = result;

    const toastLiveExample = document.getElementById('liveToast')
    const toastBootstrap = bootstrap.Toast.getOrCreateInstance(toastLiveExample)
    toastBootstrap.show()
}

// TODO:
// Переделать под покупку билета (со скидками)
//
