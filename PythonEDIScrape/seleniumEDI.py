from selenium import webdriver
import time
import html
import os
from tkinter import messagebox

seenDocs = []
for file in os.listdir("./xmldocs"):
    if file.endswith(".xml"):
        seenDocs.append(file.split('.')[0])
        
print(len(seenDocs))
options = webdriver.ChromeOptions()
#options.add_argument("--headless")  
options.add_argument('--ignore-certificate-errors')
options.add_argument("--test-type")
options.binary_location = "/usr/bin/chromium"
driver = webdriver.Chrome(chrome_options=options)
driver.get('https://cloud.freeway.opentext.com/Account/SignIn/')

driver.execute_script("UserName.value='***REMOVED***'; Password.value='***REMOVED***';buttonSubmit.removeAttribute('disabled');buttonSubmit.click()");
time.sleep(1)

driver.add_cookie(driver.get_cookies()[0])
driver.add_cookie(driver.get_cookies()[1])

i = 0
not_seen = True
while(not_seen):
	driver.get('https://cloud.freeway.opentext.com/Document')
	time.sleep(1)
	driver.execute_script('Documents_' + (str(i)) + '__Selected.click()')
	time.sleep(1)
	driver.execute_script('document.getElementsByName("viewdata")[0].click()')
	time.sleep(1)

	url_code = driver.current_url.rsplit('/', 1)[-1]
	details = driver.page_source

	print(url_code)
	for code in seenDocs:
		if(code == url_code):
			not_seen = False
	
	if(i == 5):
		not_seen = False

	if(not_seen):
		start = details.find('<xmp>')
		end = details.find('</xmp>')
		raw = details[start + 5:end]
		decoded = html.unescape(raw)


		f = open("./xmldocs/" + url_code + ".xml", "w")
		f.write(decoded)
	
	i += 1
	
print(i - 1)
	




