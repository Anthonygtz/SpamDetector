// TODO: onload function should retrieve the data needed to populate the UI
let apiCallURL = "http://localhost:8080/spamDetector-1.0/api/spam";

(function () {
  fetch(apiCallURL,
    {
      method: 'GET',
      headers: {
        'Accept': 'application/json',
      },
    })
    .then(response => response.json())
    .then(data => {
      //console.log(data)
      const table = document.getElementById("tbody");

      for (let i = 0; i < data.length; i++) {

        const row = '<tr><td>' + data[i].file + '</td><td>' + data[i].spamProbability + "</td><td>" + data[i].actualClass + "</td></tr>"
        table.innerHTML  += row;
      }

    })
    .catch((err) => {
      console.log("something went wrong: " + err);
    })
})();

(function ()
  {
    fetch("http://localhost:8080/spamDetector-1.0/api/spam/accuracy",
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      })
      .then(response => response.json())
      .then(data => {
        const accuracy = document.getElementById("accuracy");
        accuracy.value = data.accuracy;
      })
      .catch((err) => {console.log("something went wrong: " + err);});
  }
)();

(function ()
  {
    fetch("http://localhost:8080/spamDetector-1.0/api/spam/precision",
      {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      })
      .then(response => response.json())
      .then(data => {
        const precision = document.getElementById("precision");
        precision.value = data.precision;
      })
      .catch((err) => {console.log("something went wrong: " + err);});
  }
)();
