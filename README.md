# Sijoittajamestari2019
Bull or bear? Artificial neural network to the rescue.

## Usage

First, a CSV file from Yahoo finance is required. The CSV file should use
```,``` as delimiter. The headers should be Date, Open, High, Low, Close,
Adjusted Close and Volume. The name of the CSV should be ```from-yahoo.csv```.

Then, use the the Clojure app to convert the Yahoo CSV file into a new CSV file
to be used in the next step:

```bash
clj -m core
```

Then run the attached Jupyter notebook (```notebook.ipynb```) step by step to
get the next day classification. You can use my
[Dockerfile](https://github.com/MattiNieminen/dockerfiles/tree/master/keras)
to do that.
