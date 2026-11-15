"""ASTRA local development server: serves static frontend and proxies /api to Spring Boot.
Python standard library only. Run: python run_frontend.py
"""
from http.server import SimpleHTTPRequestHandler, ThreadingHTTPServer
from pathlib import Path
from urllib.request import urlopen
from urllib.error import HTTPError, URLError
import os

os.chdir(Path(__file__).resolve().parent)

class Handler(SimpleHTTPRequestHandler):
    def do_GET(self):
        if self.path == '/api' or self.path.startswith('/api/') or self.path.startswith('/api?'):
            try:
                with urlopen('http://127.0.0.1:8080' + self.path, timeout=8) as response:
                    payload = response.read()
                    self.send_response(response.status)
                    self.send_header('Content-Type', response.headers.get('Content-Type', 'application/json'))
                    self.send_header('Cache-Control', 'no-store')
                    self.end_headers()
                    self.wfile.write(payload)
            except HTTPError as err:
                self.send_response(err.code)
                self.send_header('Content-Type', err.headers.get('Content-Type', 'application/json'))
                self.end_headers()
                self.wfile.write(err.read())
            except (URLError, TimeoutError) as err:
                self.send_response(502)
                self.send_header('Content-Type', 'text/plain; charset=utf-8')
                self.end_headers()
                self.wfile.write(b'ASTRA backend unavailable at localhost:8080')
        else:
            super().do_GET()

if __name__ == '__main__':
    print('ASTRA frontend: http://localhost:5500')
    print('Backend expected: http://localhost:8080')
    ThreadingHTTPServer(('127.0.0.1', 5500), Handler).serve_forever()
