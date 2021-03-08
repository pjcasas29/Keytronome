<h1 align="center">

  <img  width="500"
        alt="Keytronome"
        src="https://github.com/pjcasas29/Keytronome/blob/master/Logo.png"/>

</h1>

Metronome application to aid in playing in different keys created using the MVVM design pattern. 
Uses LiveData for main app functionality, RxJava, Retrofit, OkHttp for social media features and Room for saving presets locally(Presets still in developemnt). 

# Motivation

This application is for any musician who would like a customizable metronome that displays all 12 keys in different orders. 

The user can select:

- Tempo
- Time signature
- Number of cycles (times the metronome will go around the same set of keys)
- Order of keys currently supporting:
    - Chromatic
    - Fifths
    - Fourths
    - Thirds
    - Whole steps
    - Random order
- Starting key
- Measures per Key (Number of measures until the next key chaange occurs)

# Screenshots

<h4>Starting app and playing with default values
</h4>

<img align="center" alt="demo startup gif" src="https://github.com/pjcasas29/Keytronome/blob/master/DemoStart.gif" height="500"/>

<h4 >Selecting Tempo</h4>
<div style="text-align:center">
<img alt="demo startup gif" src="https://github.com/pjcasas29/Keytronome/blob/master/DemoTempo.gif" height="500"/>
</div>

<h4 >Changing order of keys</h4>

<div style="text-align:center">
<img alt="demo startup gif" src="https://github.com/pjcasas29/Keytronome/blob/master/DemoOrder.gif" height="500"/>
</div>

## Maintainers
This project is mantained by:
* [Pedro Casas](http://github.com/pjcasas29)


## Contributing

1. Fork it
2. Create your feature branch (git checkout -b my-new-feature)
3. Commit your changes (git commit -m 'Add some feature')
4. Push your branch (git push origin my-new-feature)
5. Create a new Pull Request
