# To run use:  python3 DigestDictionary.py > output.txt

with open('wordlist-ao-latest.txt', encoding="latin-1") as f:
    initialWords = f.read().splitlines()
    listOfWords = list(filter(lambda x: len(x) >= 4 and len(x) <=8 and '-' not in x and x[len(x)-1] != 's' and 'w' not in x and 'y' not in x and 'k' not in x and not x.isupper(), initialWords))
    for word in listOfWords:
        print(word.upper())