import urllib.request, json
url = "https://backvynils-alternos-production.up.railway.app/albums"
req = urllib.request.Request(url)
with urllib.request.urlopen(req) as response:
    albums = json.loads(response.read().decode())
    for a in albums:
        id = a["id"]
        url_a = f"{url}/{id}"
        try:
            req_a = urllib.request.Request(url_a)
            with urllib.request.urlopen(req_a) as resp:
                data = json.loads(resp.read().decode())
                print(f"Album {id}: OK, {len(data.get('tracks', []))} tracks")
        except Exception as e:
            print(f"Album {id}: FAILED {e}")
