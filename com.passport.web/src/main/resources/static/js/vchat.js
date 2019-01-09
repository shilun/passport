var myLayer;

(function() {

    TipsLayer.instance = null;

    function TipsLayer(container) {

        if( !container ) {
            throw new Error('Container is required.');
        }

        this.layer = typeof container === 'string' ? document.querySelector(container) : container;

        if ( !this.layer instanceof HTMLElement ) {
            throw new Error('Container is not a dom element.');
        }

        this.closeBtn = this.layer.querySelector('#layer-close');

        var _this = this;
        this.closeBtn.addEventListener('click', function(e) {
            _this.hide();
        });
    }


    TipsLayer.init = function (container) {
        return TipsLayer.instance ? TipsLayer.instance : TipsLayer.instance = new TipsLayer(container);
    }


    TipsLayer.prototype.show = function() {
        this.layer.classList.remove('hide');        
    }

    TipsLayer.prototype.hide = function() {
        this.layer.classList.add('hide');
    }

    myLayer = TipsLayer.init('#tips-layer');

})();