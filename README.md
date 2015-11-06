# inc-deprimer
A simple Web API that accepts operations and a list of numbers and returns the result of applying those operations on the numbers

Getting started:
```shell
lein ring server-headless`
```

Usage:
```shell
curl "http://localhost:3000/api?numbers=1,2,3&ops=inc"
# {"result":[2,3,4]}
```
```shell
curl "http://localhost:3000/api?numbers=1,2,3&ops=removePrimes"
# {"result":[1]}
```
```shell
curl "http://localhost:3000/api?numbers=1,2,3&ops=inc,removePrimes"
# {"result":[4]}
```
```shell
curl "http://localhost:3000/api?numbers=1514,5730,7237,113,3683,9004,7112,2573,3902,1777&ops=inc,inc,inc,removePrimes,inc,inc,removePrimes"
# {"result":[1519,5735,7242,118,3688,7117,2578,1782]}
```
