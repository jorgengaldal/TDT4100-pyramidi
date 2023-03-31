# Planlegging av prosjektapp

**Appnavn:** Pyramidi (stilisert *pyramidi*)

## Synopsis

*pyramidi* er en musikkapplikasjon for å organisere musikk i en pyramideformet struktur hvor hver sang kan være i et nivå i spillelisten. 
Dette nivået tilsvarer hvor ofte sangen skal avspilles.
Sangene øverst i pyramiden spilles i en lyttesesjon av oftere enn sangene lavere i pyramiden.

## Implementasjon

### Grunnklasser

<!-- #### PlayableContainer (Abstract Class)

*Implementerer grensesnittet **Playble**.*

Klasse for å samle funksjonalitet for klasser som kan inneholde ting som man kan spille av.

##### Metoder  -->


#### Song (Class)

*Implementerer grensesnittet `Playble`.*

Dette er en dataklasse som inneholder informasjon om en sang.

##### Metoder

<!-- `Song(String title)` - konstruktør.

`Song(String title, String artist)` - konstruktør.

`Song(String title, String artist, String album)` - konstruktør. -->

- `Song(String title, String artist, String album, Calendar published, double duration)` - Konstruktør. Alle verdiene, utenom duration og title, kan være null. 

- `public String getArtist()` - gir navnet på artisten.

- `public String getTitle()` - gir navnet på sangen.

- `public String getAlbum()` - gir albumet som sangen er en del av, eller null om den ikke inngår i et album.

- `public Calendar getPublishDate()` - gir datoen sangen ble publisert.

- `public double getDuration()` - gir sanglengden, i sekunder.

- `public static Song loadFromFile(String path)` - Returnerer et Song-objekt med tilstand lastet inn fra en fil gitt av path.

- `public void saveToFile(String path)` - lagrer tilstanden sin til en fil gitt av path.

#

#### Pyramide (Class)

<!-- *Implementerer grensesnittet Playable.* -->

*Implementerer grensesnittet `Iterable`*

Dette er en klasse som inneholder lag, som hver inneholder sanger.

##### Felter

- `private List<PyramidLayer> layers` - Liste over pyramidelag.

##### Metoder

- `public void addLayer(int index)` - Legger til et nytt `PyramidLayer` på den gitte indeksen.

- `public void removeLayer(int index)` - Fjerner et lag fra pyramiden. Alle sangene faller ned til laget under.

- `public Playable getNextPlayable()` - Gir neste `Playable` som skal spilles, basert på vektene gitt av `PyramidLayer`.

- `private int getTotalLayerWeight()` - Gir totaltvekting for alle `PyramidLayer` i layers.

#

#### PyramidLayer (Class)

*Implementerer grensesnittet `Iterable`*

Denne klassen har ansvar for å holde på sanger til sitt nivå.

##### Felter

- `private List<Playable> contents` - Liste over `Playable`s på dette nivået. 

##### Metoder

- `public int getWeight()` - Tallet som brukes for å avgjøre hvor ofte sanger fra dette nivået skal spilles.

- `public Playable getElement(int index)` - Gir `Playable`-en på posisjon `index` i liste. 

- `public Playable next()` - Gir neste `Playable` i dette laget. (Fra Iterable)

- `public boolean hasNext()` - Gir i utgangspunktet alltid true, fordi hvis den er tom, går den bare på nytt.

#

#### Playable (Interface)

Dette grensesnittet brukes for å type alle klasser som kan spilles av.

##### Metoder

- `public void play()` - Er foreløpig ikke implementert.