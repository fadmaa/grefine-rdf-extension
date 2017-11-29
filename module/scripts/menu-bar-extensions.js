//extend the column header menu
$(function(){
  ExtensionBar.MenuItems.push(
    {
      "id":"rdf-extension-menu",
      "label": "RDF",
      "submenu" : [
        {
          "id": "rdf/edit-rdf-schema",
          label: "Edit RDF Skeleton...",
          click: function() { RdfSchemaMenuBar.editRdfSchema(false); }
        },
        {
          "id": "rdf/reset-rdf-schema",
          label: "Reset RDF Skeleton...",
          click: function() { RdfSchemaMenuBar.editRdfSchema(true); }
        },
        {}
      ]
    }
  );
});
