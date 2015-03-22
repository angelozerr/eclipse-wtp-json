package org.eclipse.wst.json.core.internal.schema.catalog;

import org.eclipse.wst.json.core.schema.catalog.ICatalog;
import org.eclipse.wst.json.core.schema.catalog.ICatalogElement;
import org.eclipse.wst.json.core.schema.catalog.INextCatalog;



public class NextCatalog extends CatalogElement implements INextCatalog
{
  private String location;
  private ICatalog referencedCatalog;

  public NextCatalog()
  {
    super(ICatalogElement.TYPE_NEXT_CATALOG);
  }
    
  public String getCatalogLocation()
  {
    return location;
  }

  public ICatalog getReferencedCatalog()
  {
    if (referencedCatalog == null)
    {
      referencedCatalog = ((Catalog)ownerCatalog).getCatalogSet().lookupOrCreateCatalog(getId(), getAbsolutePath(location));
    }
    return referencedCatalog;
  }

  public void setCatalogLocation(String uri)
  {
    location = uri;
    referencedCatalog = null;
  }
  
  public Object clone()
  {
	NextCatalog nextCatalog = (NextCatalog)super.clone();
	nextCatalog.setCatalogLocation(location);
    return nextCatalog;
  }
}
