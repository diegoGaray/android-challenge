# TechKAndroidDeveloperTest

dependencias:

```
dependencies {
    implementation fileTree(dir: 'libs', include: ['*.jar'])
    //Diseño layout
    implementation 'com.android.support:appcompat-v7:27.1.1'
    implementation 'com.android.support.constraint:constraint-layout:1.1.3'
    implementation 'com.android.support:cardview-v7:27.1.1'
    implementation 'com.android.support:recyclerview-v7:27.1.1'
    implementation 'com.android.support:design:27.1.1'
    //libreria para trabajar con objetos JSON
    implementation 'com.google.code.gson:gson:2.8.0'
    //libreria http response
    implementation 'com.android.volley:volley:1.0.0'
    //trabajar con imágenes:
    implementation 'com.squareup.picasso:picasso:2.71828'
    testImplementation 'junit:junit:4.12'
    androidTestImplementation 'com.android.support.test:runner:1.0.2'
    androidTestImplementation 'com.android.support.test.espresso:espresso-core:3.0.2'
}
```

![alt text](https://github.com/diegoGaray/android-challenge/blob/master/pantallazo1.png)
![alt text](https://github.com/diegoGaray/android-challenge/blob/master/pantallazo2.png)
![alt text](https://github.com/diegoGaray/android-challenge/blob/master/pantallazo3.png)
![alt text](https://github.com/diegoGaray/android-challenge/blob/master/pantallazo4.png)
![alt text](https://github.com/diegoGaray/android-challenge/blob/master/pantallazo5.png)

En esta url baso mi aplicación, en donde se muestran los post virales:
https://api.imgur.com/3/gallery/hot/viral/0.json

A considerar: la Aplicación tiene errores que “aparecen a veces”, ya que hay Tags que no tienen imágenes asociadas o extensiones  .gift y mp4 que no se muestran en un imageview. La pantalla principal muestra Tags duplicados ya que las galerías repiten los Tags, corregir eso no es difícil, pero tendría que rehacer la app de nuevo porque al eliminar los Tags duplicados se eliminan imágenes. Hay títulos que se repiten porque no todas las imágenes tienen un título asociado, así que te muestra el título de la galería.

APK: https://play.google.com/store/apps/details?id=com.garapps.diego.techkandroiddevelopertest
