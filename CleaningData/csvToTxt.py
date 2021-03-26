
import csv
from datetime import datetime

start_time = datetime.now()

with open('/Users/jeancasoto/Downloads/ghtorrent-2019-05-20.csv', 'rb') as f:
    reader = csv.reader(f)
    next(reader)  # Ignoramos nombre de la columna

    with open('/Users/jeancasoto/Downloads/usersSpace.txt', 'w') as nf:
        for row in reader:
            nf.write(row[0]+' ')

end_time = datetime.now()
print('Duration: {}'.format(end_time - start_time))
