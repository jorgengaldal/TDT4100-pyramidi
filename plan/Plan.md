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

- `Song(String title, String artist, String album, Calendar published, double duration)` - Konstruktør. Alle verdiene, utenom duration og title, kan være null. Duration må være positivt tall. 

- `public String getArtist()` - gir navnet på artisten.

- `public String getDisplayedArtist()` - gir artistnavn, eller "Ukjent artist", om getArtist() er null. Brukes i grensesnittet.

- `public String getTitle()` - gir navnet på sangen. Brukes også i grensesnittet (siden tittel ikke kan være null).

- `public String getAlbum()` - gir albumet som sangen er en del av, eller null om den ikke inngår i et album.

- `public String getDisplayedAlbum()` - gir albumnavnet. Returnerer "Ukjent album" hvis getAlbum() er null Brukes i grensesnittet. 

- `public Calendar getPublishDate()` - gir datoen sangen ble publisert.

- `public String getDisplayedPublishedDate()` - gir publiseringsdatoen i formatet DD.MM.YYYY. Returnerer "Ukjent publiseringsdato" hvis getPublishDate() er null. Brukes i grensesnittet.

- `public double getDuration()` - gir sanglengden, i sekunder.

- `public String getDisplayedDuration()` - gir sanglengden på formatet mm:ss (eventuelt hh:mm:ss, om aktuelt. <!-- TODO: Vurder om det trengs eller ikke senere. -->). Brukes i grensesnittet.

- `public static Song loadFromFile(String path)` - Returnerer et Song-objekt med tilstand lastet inn fra en fil gitt av path.

- `public void saveToFile(String path)` - lagrer tilstanden sin til en fil gitt av path.

#

#### Pyramid (Class)

<!-- *Implementerer grensesnittet Playable.* -->

*Implementerer grensesnittene `PlayableContainer` og `Iterable`*

Dette er en klasse som inneholder lag, som hver inneholder sanger (eller `Playable`s).

##### Felter

- `private HashMap<Integer, PyramidLayer> layers` - Map over pyramidelag med vekt som key.

##### Metoder

- `public void Pyramid(int weight)` - Lager en ny pyramide og starter med en pyramide med vekt `weight`. 

- `public void Pyramid()` - Lager en ny pyramide og starter med en pyramide med vekt 1.

- `public void addLayer(int weight)` - Legger til et nytt `PyramidLayer` med weight som key.

- `public PyramidLayer getLayer(int index)` - Returnerer `PyramidLayer`på nivå med vekt `weight`.

- `public void removeLayer(int index)` - Fjerner et lag fra pyramiden. Alle sangene faller ned til laget under, eventuelt over, om det er det nederste nivået som fjernes.

- `public void removeLayer(PyramidLayer layer)` - Fjerner et lag fra pyramiden. Alle sangene faller ned til laget under.

- `public void movePlayable(Playable playable, PyramidLayer from, PyramidLayer to)` - flytter `playable` fra nivå `from` til `to`

- `public void promotePlayable(Playable playable, PyramidLayer from)` - flytter `playable` fra nivå `from` til et nivå høyere.

<!-- - `public Playable getNextPlayable()` - Gir neste `Playable` som skal spilles, basert på vektene gitt av `PyramidLayer`. 
Merk: Denne blir vel heller implementert i PyramidLayerIterator -->

- `public void addPlayable(Playable playable)` - Legger til `playable` til det laveste laget.

- `private int getTotalLayerWeight()` - Gir totaltvekting for alle `PyramidLayer` i layers.

###### Implementerte metoder fra grensesnitt
<!-- 
- *Fra `Iterable`*: `next()`

- *Fra `Iterable`*: `hasNext()` -->

- *Fra `Iterable`*: `public Iterator<Playable> iterator()` - Implementasjonen returnerer en PyramidPlayableIterator av seg selv.

- *Fra `PlayableContainer`*: `public int getTotalPlayablesNumber()`

#

#### PyramidLayer (Class)

<!-- *Implementerer grensesnittet `Iterable`* -->

Denne klassen har ansvar for å holde på sanger til sitt nivå.

Kan ikke spilles av direkte i `Player`, kun gjennom en `Pyramid`

##### Felter

- `private List<Playable> contents` - Liste over `Playable`s på dette nivået. 

##### Metoder

<!-- - `public int getWeight()` - Tallet som brukes for å avgjøre hvor ofte sanger fra dette nivået skal spilles. -->

- `public int size()` - Antall sanger i PyramidLayer.

- `public double getTotalPlayTime()` - Gir summen av duration for alle `Playable` i contents.

- `public Playable get(int index)` - Gir `Playable`-en på posisjon `index` i liste. 

- `public void add(Playable playable)` - Legger til `playable` i `contents`

- `public void remove(Playable playable)` - Fjerner `playable`fra `contents`

<!-- - `public Playable next()` - Gir neste `Playable` i dette laget. (Fra Iterable)

- `public boolean hasNext()` - Gir i utgangspunktet alltid true, fordi hvis den er tom, går den bare på nytt. -->

#

#### PyramidLayerPlayableIterator (Class)

*Implementerer grensesnittet `Iterator`*

Brukes for å iterere gjennom `PyramidLayer` sine `Playable`, for eksempel (og spesielt) i `PyramidIterator`.

#

#### PyramidPlayableIterator (Class)

*Implementerer grensesnittet `Iterator`*

Brukes for å iterere gjennom `Pyramid` sine `PyramidLayer` sine `Playable`, for eksempel (og spesielt) i `Player`.

Bruker flere `PyramidLayerIterator` for å iterere gjennom basert på `Pyramid` sine `PyramidLayer` sine vekter. 
#

#### PyramidLayerIterator (Class)

*Implementerer grensesnittet `Iterator`*

Brukes for å iterere gjennom `Pyramid` sine `PyramidLayer`, for eksempel i grensesnittets oversikt.

#

#### Playable (Interface)

<!-- TODO: Vurder å ikke ta med denne og heller implementere et annet grensesnitt (typ iterator) -->

Dette grensesnittet brukes for å type de klassene som kan spilles av direkte (foreløpig kun `Song`, men kan være aktuelt for for eksempel ting som `Podcast`, om det skulle blitt implementert i fremtiden.)

##### Metoder

- `public void play()` <!--- Foreløpig mest for placeholding. -->

- `public double getDuration()`

- `public String getTitle()`

#

#### PlayableContainer (Interface)

<!-- extends iterator?
Kunne kanskje gitt mening, siden denne tross alt må kunne itereres gjennom.
Svar: Tror jeg heller implementerer Iterator i en annen klasse -->

Dette grensesnittet brukes for å type de klassene som inneholder Playables og som kan spilles av fra player. (Eksempelvis `Pyramid`, men ikke `PyramidLayer`)

##### Metoder

- `public int getTotalPlayablesNumber()` - Gir totalt antall `Playable`s den inneholder

<!-- ###### Metoder som arves fra `Iterable`

- `public Playable next()` - Gir neste `Playable`

- `public boolean hasNext()` - Sier om det er flere `Playable`s å iterere over. -->

#


#### Player

Klasse som brukes for å holde styr på den aktivt spillende sangen og playlisten.

#### Felter

- `private Playable currentSong` - Den sangen som foreløpig blir spilt.

- `private PlayableContainer currentContainer` - Den containeren (f. eks pyramide) som de neste sangene ved call til `next()` vil komme fra.

##### Metoder

- `public Playable getCurrentSong()`

- `public void next()` - Setter `currentSong` til neste sang i `currentContainer`
<!-- TODO: Fyll ut resten av dette. -->

#
### Grensesnitt

Grensesnittet bruker displayed-varianten av getter-metodene (eks. getDisplayedArtist).