# Calculating the filter coefficients with Python

Shows how to design IIRJ filters with scipy-commands. In this demo the
filter coefficients of an elliptic filter were generated with
`elliptic_design.py` and then added to the java application.

## Compilation

```
gradle build
```

## Run the code

`gradle run` calculates the impulse response and writes it
to `app/elliptic.dat`.

## Plot the frequency response

```
python3 plot_impulse_fresponse.py
```
