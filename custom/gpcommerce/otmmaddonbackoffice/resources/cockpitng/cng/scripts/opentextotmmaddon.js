function openTextOtmmAddon_performSso(openTextOtmmAddon_loginUrl) {
    try {
        var xmlhttp;
        if (XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
            xmlhttp.withCredentials = true;
            xmlhttp.open('GET', openTextOtmmAddon_loginUrl, true);
            xmlhttp.setRequestHeader("Accept", "application/json");

        } else {
            // At IE Internet Options you can find the option
            // 'Enable native XMLHTTP support'.
            // If this option is disabled you have to use
            // the ActiveXObject instead.
            xmlhttp = new ActiveXObject("MSXML2.XMLHTTP.3.0");
            xmlhttp.open('GET', openTextOtmmAddon_loginUrl, true);
        }

        xmlhttp.send();

    } catch (e) {
        // ignore errors
    }
}


function openTextOtmmAddon_performSsoAndOpenUrl(loginUrl, assetInspectorUrl) {
    try {
        var xmlhttp;
        if (XMLHttpRequest) {
            xmlhttp = new XMLHttpRequest();
            xmlhttp.withCredentials = true;
            xmlhttp.open('GET', loginUrl, true);
            xmlhttp.setRequestHeader("Accept", "application/json");

        } else {
            // At IE Internet Options you can find the option
            // 'Enable native XMLHTTP support'.
            // If this option is disabled you have to use
            // the ActiveXObject instead.
            xmlhttp = new ActiveXObject("MSXML2.XMLHTTP.3.0");
            xmlhttp.open('GET', loginUrl, true);
        }

        xmlhttp.onreadystatechange = function () {
            if (xmlhttp.readyState == 4) {
                window.open(assetInspectorUrl);
            }
        };
        xmlhttp.send();

    } catch (e) {
        // ignore errors
    }
}


function openTextOtmmAddon_sendAssetAssignmentDialogData(key, widgetId, eventName) {
    openTextOtmmAddon_overwriteTreeSelection();
    var data = localStorage.getItem(key);
    zAu.send(new zk.Event(zk.Widget.$('$' + widgetId), eventName, data, {toServer: true}));
}

function openTextOtmmAddon_setAssetAssignmentDialogData(key, data) {
    localStorage.setItem(key, data);
}

function openTextOtmmAddon_clearAssetAssignmentDialogData(key) {
    localStorage.removeItem(key);
}

function openTextOtmmAddon_overwriteTreeSelection() {

    zul.sel.Tree.prototype._select = function (row, evt, skipFocus) {
        if (this._selectOne(row, skipFocus) || row.firstChild._node.className.includes("z-treerow-selected")) {
            this.fireOnSelect(row, evt);
        }
    };
}
