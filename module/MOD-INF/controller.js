/*
 * Function invoked to initialize the extension.
 */
function init() {
  var RefineServlet = Packages.com.google.refine.RefineServlet;
  /*
   *  Attach an rdf schema to each project.
   */
  Packages.com.google.refine.model.Project.registerOverlayModel("rdfSchema", Packages.org.deri.orefine.rdf.RdfSchema);
  
  /*
  *  Commands
  */
  RefineServlet.registerCommand(module, "save-baseURI", new Packages.org.deri.orefine.rdf.commands.SaveBaseURICommand());

  var ClientSideResourceManager = Packages.com.google.refine.ClientSideResourceManager;
    
  // Script files to inject into /project page
  ClientSideResourceManager.addPaths(
    "project/scripts",
    module,
    [
      "scripts/rdf-schema/node-canvas.js",
      "scripts/rdf-schema/link-canvas.js",
      "scripts/rdf-schema/canvas.js",
      "scripts/rdf-schema/commands.js",
      "scripts/menu-bar-extensions.js"
    ]
  );
  
  // Style files to inject into /project page
  ClientSideResourceManager.addPaths(
    "project/styles",
    module,
    [
      "styles/schema-alignment-dialog.css",
      "styles/rdf-schema.css"
    ]
  );
}

function process(path, request, response) {
  if (path == "/" || path == "") {
    butterfly.redirect(request, response, "/");
  }
}
