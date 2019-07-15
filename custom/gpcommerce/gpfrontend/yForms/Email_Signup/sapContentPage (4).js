/*
 * Copyright (C) 2009-2018 SAP SE or an SAP affiliate company. All rights reserved.
 */
(function() { "use strict"; var C = { BasePath: "/sap/opu/odata/sap/CUAN_CONTENT_PAGE_RESULT_SRV/", ResultHeadersPath: "ResultHeaders", CORS: false, CSRFTokenHeader: "X-CSRF-Token", AppendScenarioParameter: "", Tenant: "", Version: "11.8.7" }; var c; var R = { getConfig: function() { return C; }, initialize: function() { if (this.isInitializing() || this.isInitialized()) { return; } this.reset(); var a = this.getPages(),
                i; for (i = 0; i < a.length; i++) { this.ContentPage.prepare(a[i]); } this.Request.fetchToken(a, this.handleFetchTokenResponse.bind(this)); }, handleFetchTokenResponse: function(a, n) { var i; if (n) { for (i = 0; i < a.length; i++) { this.Result.sendOpen(a[i], this.handleOpenResponse.bind(this)); } } else { for (i = 0; i < a.length; i++) { R.ContentPage.toggleErrorMessage(a[i], true); } } }, handleOpenResponse: function(o, r) { if (R.Request.isErrorResponse(r)) { R.ContentPage.toggleErrorMessage(o, true); } else { this.ContentPage.finishLoading(o, r); } o.sapCpInitializing = false;
            o.sapCpInitialized = true; }, isInitializing: function() { var a = this.getPages(),
                i; for (i = 0; i < a.length; i++) { if (a[i].sapCpInitializing) { return true; } } return false; }, isInitialized: function() { var a = this.getPages(),
                i; for (i = 0; i < a.length; i++) { if (!a[i].sapCpInitialized) { return false; } } return true; }, getPages: function() { return R.Node.getAllWithClassName(document, "sapCpContentPage"); }, reset: function() { var a = this.getPages(),
                i;
            c = null; for (i = 0; i < a.length; i++) { delete a[i].sapCpInitializing;
                delete a[i].sapCpInitialized; } }, ContentPage: { prepare: function(o) { if (o.sapCpInitializing || o.sapCpInitialized) { return; } o.sapCpInitializing = true;
                this.checkVersion(o);
                this.prepareWidgets(o);
                R.Event.registerListener(o, "submit", this.handleSubmitEvent.bind(this)); }, checkVersion: function(o) { var s = R.Setting.get(o, "version"); if (s > C.Version) { R.Console.warn("You are using an outdated version of the landing page script"); } }, prepareWidgets: function(o) { var w = this.getWidgets(o),
                    i; for (i = 0; i < w.length; i++) { R.Widget.prepare(w[i]); } }, getWidgets: function(o) { return R.Node.getAllWithClassName(o, "sapCpWidget"); }, checkMissingMandatoryFieldsLabel: function(n) { var o = R.Util.findParentByClassName(n, "sapCpContentPage"),
                    m = R.Node.getAllWithClassName(o, "sapCpWidgetMandatoryMissing"),
                    h = (m.length > 0);
                R.ContentPage.toggleMissingMandatoryField(o, h); }, finishLoading: function(o, r) { if (r && r.ContactPersonalizationData && r.ContactPersonalizationData.results) { this.processPersonalizationData(o, r.ContactPersonalizationData.results); } this.toggleLoading(o, false); }, processPersonalizationData: function(o, p) { var P, w = this.getWidgets(o),
                    W, i; for (i = 0; i < p.length; i++) { P = p[i];
                    W = R.Util.findWidgetByKey(w, P.WidgetKeyHash); if (W) { R.Widget.applyPersonalization(W, P.Value); } } }, handleSubmitEvent: function(e) { if (e.preventDefault) { e.preventDefault(); } }, collectAnswers: function(o, a) { var w = this.getWidgets(o),
                    W, A = [],
                    m, M = false,
                    i = false,
                    I; for (I = 0; I < w.length; I++) { W = w[I];
                    m = R.Widget.collectAnswer(W, a); if (m) { A.push(m); } else if (m === false) { M = true; } else if (m === null) { i = true; } } if (M || i) { return false; } return A; }, toggleLoading: function(o, s) { R.CSS.toggleClass(o, "sapCpContentPageLoading", s); }, toggleMissingMandatoryField: function(o, s) { R.CSS.toggleClass(o, "sapCpMissingMandatoryField", s); }, toggleInvalid: function(o, i) { R.CSS.toggleClass(o, "sapCpContentPageInvalid", i); }, toggleErrorMessage: function(o, s) { R.CSS.toggleClass(o, "sapCpSubmitError", s); }, toggleSuccessMessage: function(o, s) { R.CSS.toggleClass(o, "sapCpSubmitCompleted", s); }, isProductiveTestMode: function(o) { var t = R.Setting.get(o, "test-mode"),
                    p = R.Setting.get(o, "productive"); return (t && p); }, setErrorMessage: function(o, e) { var E = R.Node.getFirstWithClassName(o, "sapCpErrorMessageText"); if (E) { E.sapCpErrorMessage = (E.sapCpErrorMessage || E.textContent);
                    E.textContent = (e || E.sapCpErrorMessage); } } }, Layout: {}, Widget: { prepare: function(w) { if (this.isInputWidget(w)) { R.InputWidget.prepare(w); } else if (this.isCheckBoxWidget(w)) { R.CheckBoxWidget.prepare(w); } else if (this.isDownloadWidget(w)) { R.DownloadWidget.prepare(w); } else if (this.isButtonWidget(w)) { R.ButtonWidget.prepare(w); } else if (this.isInteractionWidget(w)) { R.InteractionWidget.prepare(w); } }, applyPersonalization: function(w, v) { if (this.isInputWidget(w)) { R.InputWidget.applyPersonalization(w, v); } else if (this.isCheckBoxWidget(w)) { R.CheckBoxWidget.applyPersonalization(w, v); } }, collectAnswer: function(w, o) { var a; if (this.isInputWidget(w)) { a = R.InputWidget.collectAnswer(w); } else if (this.isNoteWidget(w)) { a = R.NoteWidget.collectAnswer(w); } else if (this.isCheckBoxWidget(w)) { a = R.CheckBoxWidget.collectAnswer(w); } else if (this.isDownloadWidget(w)) { a = R.DownloadWidget.collectAnswer(w); } else if (this.isButtonWidget(w)) { a = R.ButtonWidget.collectAnswer(w, o); } else if (this.isInteractionWidget(w)) { a = R.InteractionWidget.collectAnswer(w); } if (a === false) { R.Widget.toggleMissingMandatory(w, true);
                    R.Widget.toggleInvalid(w, false); } else if (a === null) { R.Widget.toggleMissingMandatory(w, false);
                    R.Widget.toggleInvalid(w, true); } else { R.Widget.toggleMissingMandatory(w, false);
                    R.Widget.toggleInvalid(w, false); } return a; }, isMandatory: function(w) { return R.CSS.hasClass(w, "sapCpWidgetMandatory"); }, isTextWidget: function(w) { return R.CSS.hasClass(w, "sapCpTextWidget"); }, isInputWidget: function(w) { return R.CSS.hasClass(w, "sapCpInputWidget"); }, isNoteWidget: function(w) { return R.CSS.hasClass(w, "sapCpNoteWidget"); }, isCheckBoxWidget: function(w) { return R.CSS.hasClass(w, "sapCpCheckBoxWidget"); }, isDownloadWidget: function(w) { return R.CSS.hasClass(w, "sapCpDownloadWidget"); }, isButtonWidget: function(w) { return R.CSS.hasClass(w, "sapCpButtonWidget"); }, isInteractionWidget: function(w) { return R.CSS.hasClass(w, "sapCpInteractionWidget"); }, toggleMissingMandatory: function(w, m) { R.CSS.toggleClass(w, "sapCpWidgetMandatoryMissing", m); }, toggleInvalid: function(w, i) { R.CSS.toggleClass(w, "sapCpWidgetInvalid", i); } }, InputWidget: { prepare: function(i) { var I = R.Widget.isMandatory(i),
                    a, d, D, b;
                d = i.getElementsByTagName("select"); for (b = 0; b < d.length; b++) { R.Event.registerListener(d[b], "change", this.handleDropDownChangeEvent.bind(this)); } D = R.Node.getFirstWithClassName(i, "sapCpDatePicker"); if (D) { this.prepareDatePicker(D); } if (!I) { return; } a = i.getElementsByTagName("input"); if (a[0]) { R.Event.registerListener(a[0], "focusout", this.handleInputFocusOutEvent.bind(this)); } }, prepareDatePicker: function(d) { var D = R.Node.getAllWithClassName(d, "sapCpDropDown"),
                    o, i; for (i = 0; i < D.length; i++) { o = D[i]; if (R.CSS.hasClass(o, "sapCpDatePickerDay")) { this.prepareDatePickerDayDropDown(o); } else if (R.CSS.hasClass(o, "sapCpDatePickerMonth")) { R.Event.registerListener(o, "change", this.handleDatePickerDropDownChangeEvent.bind(this)); } else if (R.CSS.hasClass(o, "sapCpDatePickerYear")) { this.prepareDatePickerYearDropDown(o);
                        R.Event.registerListener(o, "change", this.handleDatePickerDropDownChangeEvent.bind(this)); } } }, prepareDatePickerDayDropDown: function(d) { var i; for (i = 1; i <= 31; i++) { this.addDatePickerDropDownOption(d, i); } }, prepareDatePickerYearDropDown: function(y) { var d = R.Setting.get(y, "dropdowntype"),
                    Y = d.split("-"),
                    i, a, o = new Date(),
                    b = o.getFullYear(),
                    e, I; if (Y.length === 4) { i = (parseInt(Y[2], 10) || 0);
                    a = (parseInt(Y[3], 10) || 0); } if (i > 0) { for (I = i; I > 0; I--) { e = b + I;
                        this.addDatePickerDropDownOption(y, e); } } this.addDatePickerDropDownOption(y, b); if (a > 0) { for (I = 1; I <= a; I++) { e = b - I;
                        this.addDatePickerDropDownOption(y, e); } } }, applyPersonalization: function(i, v) { var I = R.Node.getFirstWithClassName(i, "sapCpInput"),
                    o = R.Node.getFirstWithClassName(i, "sapCpCheckBox"),
                    d = R.Node.getFirstWithClassName(i, "sapCpDatePicker"),
                    D = R.Node.getFirstWithClassName(i, "sapCpDropDown"),
                    a; if (!v) { return; } if (I) { I.value = v; } else if (o) { a = o.getElementsByTagName("input"); if (a[0]) { a[0].checked = !!v; } } else if (d) { if (v !== "00000000") { var y = R.Node.getFirstWithClassName(d, "sapCpDatePickerYear"),
                            m = R.Node.getFirstWithClassName(d, "sapCpDatePickerMonth"),
                            b = R.Node.getFirstWithClassName(d, "sapCpDatePickerDay"); if (y) { y.value = v.substring(0, 4); } if (m) { m.value = v.substring(4, 6); } if (b) { b.value = v.substring(6, 8); } } } else if (D) { D.value = v;
                    this.updateDropDownValue(D); } }, handleInputFocusOutEvent: function(e) { var i = e.target,
                    I = R.Util.findParentByClassName(i, "sapCpInputWidget"); if (i.value) { R.Widget.toggleMissingMandatory(I, false); } R.ContentPage.checkMissingMandatoryFieldsLabel(I); }, handleDropDownChangeEvent: function(e) { var d = e.target;
                this.updateDropDownValue(d); }, handleDatePickerDropDownChangeEvent: function(e) { var d = e.target,
                    D = d.parentNode;
                this.updateDatePickerValues(D); }, updateDropDownValue: function(d) { R.CSS.toggleClass(d, "sapCpDropDownPlaceholder", !d.value); }, updateDatePickerValues: function(d) { var D = R.Node.getFirstWithClassName(d, "sapCpDatePickerDay"),
                    m = R.Node.getFirstWithClassName(d, "sapCpDatePickerMonth"),
                    y = R.Node.getFirstWithClassName(d, "sapCpDatePickerYear"); if (!D || !m || !y) { return; } var s = D.selectedOptions[0].value,
                    S = m.selectedOptions[0].value,
                    a = y.selectedOptions[0].value,
                    i = (parseInt(s, 10) || 0),
                    b = (parseInt(S, 10) || 0),
                    e = (parseInt(a, 10) || 0),
                    o = new Date(e, b, 0),
                    f = o.getDate(),
                    O = D.options.length,
                    g, I; if (i > f) { D.selectedIndex = (f - 1).toString(); } if (O > f) { for (I = O; I > f; I--) { D.remove(I - 1); } } else if (O < f) { for (I = O; I < f; I++) { g = I + 1;
                        this.addDatePickerDropDownOption(D, g); } } }, addDatePickerDropDownOption: function(d, v) { var V = v.toString(),
                    n; if (v < 10) { V = "0" + V; } n = new Option(V, V);
                d.add(n); }, checkValidity: function(i) { var r; var v = true; var I = ""; if (i.type === "email") { if (i.value) { r = new RegExp(/^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
                        v = r.test(i.value); if (!v) { I = i.getAttribute("data-sap-cp-validationMessage"); } } } if (i.type === "tel") { if (i.value) { r = new RegExp("^\\+(?:[0-9] ?){6,28}[0-9]$");
                        v = r.test(i.value); if (!v) { I = i.getAttribute("data-sap-cp-validationMessage"); } } } if (i.setCustomValidity) { i.setCustomValidity(I);
                    i.title = I; } else { i.title = I; } return v; }, collectAnswer: function(i) { var I = R.Widget.isMandatory(i),
                    o = R.Node.getFirstWithClassName(i, "sapCpInput"),
                    a = R.Node.getFirstWithClassName(i, "sapCpCheckBox"),
                    d = R.Node.getFirstWithClassName(i, "sapCpDatePicker"),
                    D = R.Node.getFirstWithClassName(i, "sapCpDropDown"),
                    b, s, e; if (o) { e = !this.checkValidity(o);
                    s = o.value; } else if (a) { b = a.getElementsByTagName("input"); if (b[0]) { s = (b[0].checked ? "X" : ""); } } else if (d) { var y = R.Node.getFirstWithClassName(d, "sapCpDatePickerYear"),
                        m = R.Node.getFirstWithClassName(d, "sapCpDatePickerMonth"),
                        f = R.Node.getFirstWithClassName(d, "sapCpDatePickerDay"); if (y && m && f) { s = y.value + m.value + f.value; } if (!/^[0-9]{8}$/.test(s)) { s = ""; } } else if (D) { s = D.value; } if (I && !s) { return false; } if (e) { return null; } var A = { WidgetKeyHash: R.Setting.get(i, "key"), WidgetValueKeyHash: "", Value: s }; return A; } }, CheckBoxWidget: { prepare: function(o) { var i = R.Widget.isMandatory(o),
                    a; if (!i) { return; } a = o.getElementsByTagName("input")[0]; if (a) { R.Event.registerListener(a, "focusout", this.handleCheckBoxFocusOutEvent.bind(this)); } }, applyPersonalization: function(o, v) { var a;
                a = o.getElementsByTagName("input")[0]; if (a) { a.checked = (a.checked || !!v); } }, handleCheckBoxFocusOutEvent: function(e) { var o = e.target,
                    a = R.Util.findParentByClassName(o, "sapCpCheckBoxWidget"); if (o.checked) { R.Widget.toggleMissingMandatory(a, false); } R.ContentPage.checkMissingMandatoryFieldsLabel(a); }, collectAnswer: function(o) { var i = R.Widget.isMandatory(o),
                    a = o.getElementsByTagName("input")[0],
                    b = false; if (a) { b = a.checked; } if (i && !b) { return false; } var A = { WidgetKeyHash: R.Setting.get(o, "key"), WidgetValueKeyHash: "", Value: (b ? "X" : "") }; return A; } }, NoteWidget: { collectAnswer: function(n) { var w = R.Widget.isMandatory(n),
                    t = n.getElementsByTagName("textarea"),
                    N; if (t[0]) { N = t[0].value; } if (w && !N) { return false; } var a = { WidgetKeyHash: R.Setting.get(n, "key"), WidgetValueKeyHash: "", Value: N }; return a; } }, DownloadWidget: { prepare: function(d) { var w = R.Widget.isMandatory(d),
                    a; if (!w) { return; } a = d.getElementsByTagName("input"); if (a[0]) { R.Event.registerListener(a[0], "focusout", this.handleCheckBoxFocusOutEvent.bind(this)); } }, handleCheckBoxFocusOutEvent: function(e) { var o = e.target,
                    d = R.Util.findParentByClassName(o, "sapCpDownloadWidget"); if (o.checked) { R.Widget.toggleMissingMandatory(d, false); } R.ContentPage.checkMissingMandatoryFieldsLabel(d); }, collectAnswer: function(d) { var w = R.Widget.isMandatory(d),
                    a = d.getElementsByTagName("input"),
                    b = false; if (a[0]) { b = a[0].checked; } if (w && !b) { return false; } var A = { WidgetKeyHash: R.Setting.get(d, "key"), WidgetValueKeyHash: "", Value: (b ? "X" : "") }; return A; } }, InteractionWidget: { prepare: function(i) { var w = R.Widget.isMandatory(i),
                    a; if (!w) { return; } a = i.getElementsByTagName("input"); if (a[0]) { R.Event.registerListener(a[0], "focusout", this.handleCheckBoxFocusOutEvent.bind(this)); } }, handleCheckBoxFocusOutEvent: function(e) { var o = e.target,
                    i = R.Util.findParentByClassName(o, "sapCpInteractionWidget"); if (o.checked) { R.Widget.toggleMissingMandatory(i, false); } R.ContentPage.checkMissingMandatoryFieldsLabel(i); }, collectAnswer: function(i) { var w = R.Widget.isMandatory(i),
                    a = i.getElementsByTagName("input"),
                    b = false; if (a[0]) { b = a[0].checked; } if (w && !b) { return false; } var A = { WidgetKeyHash: R.Setting.get(i, "key"), WidgetValueKeyHash: "", Value: (b ? "X" : "") }; return A; } }, ButtonWidget: { prepare: function(b) { var B;
                B = b.getElementsByTagName("button"); if (B[0]) { R.Event.registerListener(B[0], "click", this.handleButtonClickEvent.bind(this)); } }, handleButtonClickEvent: function(e) { var b = e.target,
                    B = R.Util.findParentByClassName(b, "sapCpButtonWidget"); if (e.preventDefault) { e.preventDefault(); } R.Result.sendSubmit(B); }, collectAnswer: function(b, o) { var a; if (b === o) { a = { WidgetKeyHash: R.Setting.get(b, "key"), WidgetValueKeyHash: "", Value: "X" }; } return a; }, performFollowUpAction: function(b, o, f) { var F = R.Setting.get(b, "follow-up-action"); if (!F || F === R.Constants.FollowUpAction.FollowUpPage) { this.openFollowUpPage(f); } else if (F === R.Constants.FollowUpAction.SuccessMessage) { R.ContentPage.toggleSuccessMessage(o, true); } }, openFollowUpPage: function(f) { R.Util.openPage(window, f); }, toggleDownloadLinkVisible: function(l, s) { R.CSS.toggleClass(l, "sapCpButtonWidgetDownloadLinkVisible", s); }, toggleLoading: function(b, l) { var L = (typeof l === "undefined" ? !R.CSS.hasClass(b, "sapCpButtonWidgetLoading") : l),
                    B = b.getElementsByTagName("button"),
                    i;
                R.CSS.toggleClass(b, "sapCpButtonWidgetLoading", L); for (i = 0; i < B.length; i++) { if (L) { B[i].setAttribute("disabled", "disabled"); } else { B[i].removeAttribute("disabled"); } } } }, Result: { sendOpen: function(o, f) { var p = (R.Setting.get(o, "prefill-data") === "true"),
                    O = R.Result.buildOpen(o, p);
                R.Request.postResult(O, function(r) { f(o, r); }); if (!p) { f(o); } }, sendSubmit: function(o) { var a = R.Util.findParentByClassName(o, "sapCpContentPage"),
                    s = R.Result.buildSubmit(a, o),
                    v = true;
                R.ContentPage.toggleErrorMessage(a, false);
                R.ContentPage.toggleSuccessMessage(a, false); if (a.checkValidity) { v = a.checkValidity();
                    R.ContentPage.toggleInvalid(a, !v); if (a.reportValidity) { a.reportValidity(); } } if (v && s) { R.ContentPage.toggleMissingMandatoryField(a, false);
                    R.ButtonWidget.toggleLoading(o, true);
                    R.Request.postResult(s, function(r) { R.Result.handleSubmitResponse(r, a, o);
                        R.ButtonWidget.toggleLoading(o, false); }); } else { R.ContentPage.toggleMissingMandatoryField(a, true); } }, buildOpen: function(o, p) { var r = this.build("OPEN", o); if (p) { r.ContactPersonalizationData = []; } return r; }, buildSubmit: function(o, a) { var r, A, m;
                r = this.build("SUBMIT", o);
                A = R.ContentPage.collectAnswers(o, a);
                m = !A;
                R.ContentPage.toggleMissingMandatoryField(o, m); if (m) { return null; } r.Answers = A;
                r.ResultValues = []; return r; }, build: function(e, o) { var l = o.getElementsByClassName("sapCpLayout")[0]; var r = { ResultEvent: e, ContentPageKeyHash: R.Setting.get(o, "key"), LayoutKeyHash: R.Setting.get(l, "key"), Url: R.Util.getCurrentUrl() }; if (R.ContentPage.isProductiveTestMode(o)) { r.ProductiveTestMode = true; } var O = R.Util.getOutboundId(); if (O) { r.OutboundId = O; } var s = R.Util.getCampaignId(); if (s) { r.CampaignId = s; } return r; }, handleSubmitResponse: function(r, o, a) { if (R.Request.isErrorResponse(r)) { var e = (r.error && r.error.message && r.error.message.value);
                    R.Console.warn(e);
                    R.ContentPage.toggleErrorMessage(o, true); return; } if (r.ResultValues && r.ResultValues.results) { this.handleResultValues(r.ResultValues.results, a); } R.ButtonWidget.performFollowUpAction(a, o, r.FollowUpPage); }, handleResultValues: function(r, o) { var m, i; for (i = 0; i < r.length; i++) { m = r[i]; if (m.WidgetValueName === R.Constants.WidgetValueName.DownloadURI) { this.handleDownloadURI(m, o); } } }, handleDownloadURI: function(d, b) { var D = d.Value,
                    l, L, s, i;
                R.Util.openDownload(window, D);
                l = R.Node.getAllWithClassName(b, "sapCpLink"); for (i = 0; i < l.length; i++) { L = l[i];
                    s = R.Setting.get(L, "download-key"); if (s === d.WidgetKeyHash) { L.href = D;
                        R.ButtonWidget.toggleDownloadLinkVisible(L, true); break; } } } }, Request: { build: function(m, p) { var M = m,
                    P = this.appendScenarioParameter(p),
                    h = new XMLHttpRequest(); if (C.CORS && !("withCredentials" in h)) { if (typeof XDomainRequest === "function") { h = new XDomainRequest(); if (M === "HEAD") { M = "GET"; } } else { R.Console.warn("Cross-Domain requests are not supported in your browser."); return null; } } h.open(M, P, true);
                this.setRequestHeaders(h);
                h.withCredentials = true; return h; }, appendScenarioParameter: function(p) { var P = p,
                    m = R.getConfig(); if (m.AppendScenarioParameter) { var s = "scenario=LPI",
                        t = "tenant=" + m.Tenant,
                        a = [s, t].join("&");
                    P += "?" + m.AppendScenarioParameter + "=" + btoa(a); } return P; }, setRequestHeaders: function(h) { if (h.setRequestHeader) { var m = R.getConfig();
                    h.setRequestHeader("Content-Type", "application/json");
                    h.setRequestHeader("Accept", "application/json"); if (m.CSRFTokenHeader) { h.setRequestHeader(m.CSRFTokenHeader, (c || "Fetch")); } return true; } return false; }, send: function(h, d, s, e) { if (h instanceof XMLHttpRequest) { h.onreadystatechange = function() { if (h.DONE && h.readyState !== h.DONE || h.readyState !== XMLHttpRequest.DONE) { return; } if (h.status >= 200 && h.status < 300) { s(h); } else { e(h); } }; } else { h.onload = function() { s(h); };
                    h.onerror = function() { e(h); };
                    h.onprogress = function() {};
                    h.ontimeout = function() {}; } if (d) { h.send(JSON.stringify(d)); } else { h.send(); } }, fetchToken: function(a, f, u) { var m = R.getConfig(),
                    h = (u ? "GET" : "HEAD"),
                    H = this.build(h, m.BasePath); if (!m.CSRFTokenHeader) { f(a, true); return; } this.send(H, null, function() { if (H.getResponseHeader) { var n = H.getResponseHeader(m.CSRFTokenHeader); if (n) { c = n; } } f(a, n || true); }, function() { if (!u) { R.Request.fetchToken(a, f, true); } else { R.Console.warn("Technical error occurred.");
                        f(a, false); } }); }, postResult: function(r, f) { var s = C.BasePath + C.ResultHeadersPath,
                    h = this.build("POST", s);
                this.send(h, r, function() { var m = JSON.parse(h.responseText); if (typeof m === "string") { m = JSON.parse(m); } m = (m && m.d || m);
                    f(m); }, function() { R.Console.warn("Technical error occurred."); var m = JSON.parse(h.responseText);
                    f(m); }); }, isErrorResponse: function(r) { if (typeof r === "undefined") { return false; } return !!r.error; }, hasCSRFToken: function() { return !!c; } }, Setting: { get: function(n, s) { return n.getAttribute("data-sap-cp-" + s); } }, Node: { getAllWithClassName: function(n, s) { if (n.getElementsByClassName) { return n.getElementsByClassName(s); } if (n.querySelectorAll) { return n.querySelectorAll("." + s); } R.Console.warn("Browser not supported!"); return []; }, getFirstWithClassName: function(n, s) { var N = R.Node.getAllWithClassName(n, s); if (N && N[0]) { return N[0]; } return null; } }, CSS: { getClasses: function(n) { if (n.classList) { return n.classList; } var s = n.getAttribute("class"); return s.split(" "); }, setClasses: function(n, a) { var s = a.join(" ");
                n.setAttribute("class", s); }, hasClass: function(n, s) { if (n.classList && n.classList.contains) { return n.classList.contains(s); } var a = this.getClasses(n); var i = a.indexOf(s); return (i >= 0); }, addClass: function(n, s) { if (n.classList && n.classList.add) { n.classList.add(s); return; } var h = this.hasClass(n, s); if (h) { return; } var a = this.getClasses(n);
                a.push(s);
                this.setClasses(n, a); }, removeClass: function(n, s) { if (n.classList && n.classList.remove) { n.classList.remove(s); return; } var h = this.hasClass(n, s); if (!h) { return; } var a = this.getClasses(n); var i = a.indexOf(s);
                a.splice(i, 1);
                this.setClasses(n, a); }, toggleClass: function(n, s, a) { var A = a; if (typeof A === "undefined") { A = !this.hasClass(n, s); } if (A) { return this.addClass(n, s); } return this.removeClass(n, s); } }, Util: { findParentByClassName: function(n, s) { if (!n || !s) { return null; } if (R.CSS.hasClass(n, s)) { return n; } var p = n.parentNode; if (!p || p === n) { return null; } return this.findParentByClassName(p, s); }, findWidgetByKey: function(w, W) { var o, i; for (i = 0; i < w.length; i++) { o = w[i]; if (R.Setting.get(o, "key") === W) { return o; } } return null; }, getCurrentUrl: function() { if (window.location !== window.top.location) { return document.referrer; } return window.location.href; }, getOutboundId: function() { return this.getURLParameter("sap-outbound-id"); }, getCampaignId: function() { return this.getURLParameter("sap-campaign-id"); }, getURLParameter: function(p) { var q = window.location.search.substring(1).split("&"); for (var i = 0; i < q.length; i++) { var P = q[i].split("="),
                        n = decodeURIComponent(P[0]); if (n === p) { return decodeURIComponent(P[1]); } } return null; }, prefixHttpProtocol: function(u) { var s = u.indexOf("/"),
                    d = u.indexOf("."),
                    p = u.indexOf("://"); if (s === 0 || d === 0) { return u; } if (p < 0 || s !== p + 1) { return "http://" + u; } return u; }, openPage: function(w, p) { if (!p) { return; } var P = R.Util.prefixHttpProtocol(p);
                w.location.href = P; }, openDownload: function(w, d) { if (!d) { return; } var D = this.prefixHttpProtocol(d);
                w.open(D, "_blank"); } }, Event: { registerListener: function(n, e, l) { if (n.addEventListener) { return n.addEventListener(e, l); } if (n.attachEvent) { return n.attachEvent("on" + e, l); } R.Console.warn("Browser not supported!"); return false; } }, Console: { warn: function(m) { if (window.console && window.console.warn) { window.console.warn(m); } } }, Constants: { WidgetValueName: { DownloadURI: "DOWNLOAD_URI" }, FollowUpAction: { FollowUpPage: "01", SuccessMessage: "02" } } };
    window.sap = (window.sap || {});
    window.sap.hpa = (window.sap.hpa || {});
    window.sap.hpa.cei = (window.sap.hpa.cei || {});
    window.sap.hpa.cei.cntpg = (window.sap.hpa.cei.cntpg || {});
    window.sap.hpa.cei.cntpg.run = (window.sap.hpa.cei.cntpg.run || R); if (window.sap.hpa.cei.cntpg.testEnvironment) { return; } R.Event.registerListener(document, "DOMContentLoaded", function() { R.initialize(); }); if (document.readyState === "complete" || document.readyState === "loaded" || document.readyState === "interactive") { R.initialize(); } }());