# Elliptic filter with Python design

Shows how to use coefficients designed by scipy's commands
and then used with the IIRJ library. The filter coefficients
were generated with `elliptic_design.py` and then added to
the java application manually.

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
