<table>
	<tbody>
		<tr>
			<td width="210px"><img src="https://github.com/paulevsGitch/BHCore/blob/main/icon.png"/></td>		
			<td>
				<h2 align="left">BHCore</h2>
				<a href="https://jitpack.io/#paulevsGitch/BHCore"><img src="https://jitpack.io/v/paulevsGitch/BHCore.svg"></a>
				<p>
					Core mod for Beta Horizons pack.
					Basic module that contains some common functions, operations, API and other similar things.
					Contains API for custom chunk section data.
				</p>
				<p>
					Dependencies:
					<ul>
						<li><a href="https://github.com/ModificationStation/StationAPI">Station API</a></li>
					</ul>
				</p>
			</td>		
		</tr>
	</tbody>
</table>

## Utilities
- BlocksUtil - allows set blockstates/get blockstates from world, convert integer facing to direction;
- BurnableUtil - allows to register blocks that can be burned by fire;
- Client util - some useful client functions and "update area/block" methods;
- DisposeUtil - utility to register Disposable objects;
- MathUtil - a set of mathematical functions and Direction arrays;
- ModUtil - utility to get current mods and their content;
- ResourceUtil - contains functions to operate with resources and reading text;
- ToolUtil - has tool tags, methods to add and check them.

## Data Storage
- SectionDataHandler - allows to add custom data to sections (arrays)
- ArraySectionData (from byte to longs and enums) - different section data arrays
- Vec2F, Vec3F, Vec3D, Vec3I - mutable vectors with different operations
- Matrix4x4 - matrix, can be used for vector rotation and in shaders

## Rendering
- Vertex, Geometry and Fragment shaders
- Shader programs
- Configurable Texture2D
- Uniforms
- Frame Buffers

## Noise
- OpenSimplexNoise (by Kurt Spencer)