


const PROXY_CONFIG = {
  "/api": {
    "target": "http://main.passport.movie.com:8000",
    "secure": false,
    "changeOrigin": true,
    "logLevel": "debug",
    "pathRewrite": {
      "^/api": "/"
    },
    "bypass": function (req, res, proxyOptions) {
      req.headers["host"] = "race";
      req.headers["referer"] = "https://raceqa.saic-gm.com/";
    }
  }
}

module.exports = PROXY_CONFIG;



